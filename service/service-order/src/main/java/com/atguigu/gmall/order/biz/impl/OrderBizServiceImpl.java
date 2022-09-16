package com.atguigu.gmall.order.biz.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuDetailFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.CartInfoVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;

import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    WareFeignClient wareFeignClient;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 获取订单确认页的数据
     *
     * @return
     */
    @Override
    public OrderConfirmDataVo getOrderConfirmData() {
        OrderConfirmDataVo vo = new OrderConfirmDataVo();

        //获取购物车中选中的商品
        List<CartInfo> data = cartFeignClient.getChecked().getData();

        List<CartInfoVo> infoVos = data.stream()
                .map(cartInfo -> {
                    CartInfoVo infoVo = new CartInfoVo();
                    infoVo.setSkuId(cartInfo.getSkuId());
                    infoVo.setImgUrl(cartInfo.getImgUrl());
                    infoVo.setSkuName(cartInfo.getSkuName());
                    //实时价格
                    Result<BigDecimal> price = skuDetailFeignClient.get1010SkuPrice(cartInfo.getSkuId());

                    infoVo.setOrderPrice(price.getData());
                    infoVo.setSkuNum(cartInfo.getSkuNum());

                    //查商品库存
                    String stock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
                    infoVo.setHasStock(stock);

                    return infoVo;
                }).collect(Collectors.toList());

        vo.setDetailArrayList(infoVos);

        //统计商品的总数量
        Integer totalNum = infoVos.stream().map(CartInfoVo::getSkuNum)
                .reduce((o1, o2) -> o1 + o2)
                .get();
        vo.setTotalNum(totalNum);
        //统计商品的总金额
        BigDecimal totalAmount = infoVos.stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum() + "")))
                .reduce((o1, o2) -> o1.add(o2)).get();
        vo.setTotalAmount(totalAmount);
        //获取用户的收货地址
        Result<List<UserAddress>> userAddressList = userFeignClient.getUserAddressList();
        vo.setUserAddressList(userAddressList.getData());

        //5.1、订单的唯一追踪号，对外交易号（和第三方交互）。
        //5.2、用来防重复提交。 做防重令牌
        String tradeNo = generateTradeNo();
        vo.setTradeNo(tradeNo);

        return vo;
    }

    /**
     * 生成追踪号
     * @return
     */
    @Override
    public String generateTradeNo() {
        long millis = System.currentTimeMillis();
        UserAuthInfo info = AuthUtils.getCurrentAuthInfo();
        String tradeNo = millis + "_" + info.getUserId();

        //令牌redis存一份
        redisTemplate.opsForValue()
                .set(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo,"1",15, TimeUnit.MINUTES);

        return tradeNo;
    }

    /**
     * 校验令牌
     */
    @Override
    public boolean checkTradeNo(String tradeNo) {
        //1、先看有没有，如果有就是正确令牌, 1, 0 。脚本校验令牌
        String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";

        /**
         * RedisScript<T> script,
         * List<K> keys, Object... args
         */
        Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class),
                Arrays.asList(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo),
                new String[]{"1"});

        if(execute > 0){
            //令牌正确，并且已经删除
            return true;
        }
//        String val = redisTemplate.opsForValue().get(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo);
//        if(!StringUtils.isEmpty(val)){
//            //redis有这个令牌。校验成功
//            redisTemplate.delete(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo);
//            return true;
//        }

        return false;

    }

    /**
     * 提交订单
     * @param submitVo
     * @param tradeNo
     * @return
     */
    @Override
    public Long submitOrder(OrderSubmitVo submitVo, String tradeNo) {
        //验证令牌
        boolean checkTradeNo = checkTradeNo(tradeNo);
        if (!checkTradeNo){
            throw new GmallException(ResultCodeEnum.TOKEN_INVAILD);
        }

        //验证库存
        ArrayList<String>  noStockSku= new ArrayList<>();
        for (CartInfoVo infoVo : submitVo.getOrderDetailList()) {
            Long skuId = infoVo.getSkuId();
            String stock = wareFeignClient.hasStock(skuId, infoVo.getSkuNum());
            if (!"1".equals(stock)) {
                noStockSku.add(infoVo.getSkuName());
            }

            if (noStockSku.size() > 0) {
                GmallException exception = new GmallException(ResultCodeEnum.ORDER_NO_STOCK);
                String skuNames = noStockSku.stream()
                        .reduce((s1, s2) -> s1 + "" + s2)
                        .get();
                throw new GmallException(
                        ResultCodeEnum.ORDER_NO_STOCK.getMessage() + skuNames,
                        ResultCodeEnum.ORDER_NO_STOCK.getCode());
            }
        }
            //3、验价格
            List<String> skuNames = new ArrayList<>();
            for (CartInfoVo infoVo : submitVo.getOrderDetailList()) {

                Result<BigDecimal> price = skuDetailFeignClient.get1010SkuPrice(infoVo.getSkuId());

                if(!price.getData().equals(infoVo.getOrderPrice())){
                    skuNames.add(infoVo.getSkuName());
                }
            }
            if(skuNames.size() > 0){
                String skuName = skuNames.stream()
                        .reduce((s1, s2) -> s1 + " " + s2)
                        .get();
                //有价格发生变化的商品
                throw  new GmallException(
                        ResultCodeEnum.ORDER_PRICE_CHANGED.getMessage() + "<br/>" +skuName,
                        ResultCodeEnum.ORDER_PRICE_CHANGED.getCode());
            }
            //4.保存到数据库
         Long orderId = orderInfoService.saveOrder(submitVo,tradeNo);

            //清空购物车中的商品
        cartFeignClient.deleteChecked();

        //1.关单-45min不支付就要关闭。
//        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
//        pool.schedule(()->{
//            closeOrder(orderId);
//        },45,TimeUnit.MINUTES);
        //2.关单-通过rabbitmq发送消息

        return orderId;
    }

    @Override
    public void closeOrder(Long userId, Long orderId){
        ProcessStatus closed = ProcessStatus.CLOSED;
        List<ProcessStatus> expected = Arrays.asList(ProcessStatus.UNPAID,ProcessStatus.FINISHED);
        //如果是未支付或者已结束才可以关闭订单 CAS
        orderInfoService.changeOrderStatus(orderId,userId,closed,expected);

    }
}
