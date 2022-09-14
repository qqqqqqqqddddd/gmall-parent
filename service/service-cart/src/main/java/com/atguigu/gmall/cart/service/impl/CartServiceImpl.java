package com.atguigu.gmall.cart.service.impl;
import java.math.BigDecimal;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.Jsons;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.feign.product.SkuDetailFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class CartServiceImpl  implements CartService {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;


    /**
     * 添加购物车
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public SkuInfo addToCart(Long skuId, Integer num) {

        String cartKey =determinCartKey();
        //添加商品到购物车
        SkuInfo skuInfo = addItemToCart(skuId,num,cartKey);

       //设置购物车的时间
        UserAuthInfo authInfo = AuthUtils.getCurrentAuthInfo();
        if ( authInfo.getUserId() == null){
           //未登录状态的操作
            String tempKey = SysRedisConst.CART_KEY + authInfo.getUserTempId();
            //设置临时购物车的过期时间90天
            redisTemplate.expire(tempKey,90, TimeUnit.DAYS);

        }

        return skuInfo;
    }

    /**
     * 将指定商品添加到购物车
     * @param skuId
     * @param num
     * @param cartKey
     * @return
     */
    @Override
    public SkuInfo addItemToCart(Long skuId, Integer num, String cartKey) {

        //拿到购物车
        BoundHashOperations<String, String, String> cart = redisTemplate.boundHashOps(cartKey);
        Boolean hashKey = cart.hasKey(skuId.toString());

        //拿到当前购物车的商品数量
        Long itemsSize = cart.size();

        //这个skuId之前没有添加过,新增
        if(!hashKey){
            if (itemsSize + 1 > SysRedisConst.CART_ITEMS_LIMIT){
                //异常机制
                throw new GmallException(ResultCodeEnum.CART_OVERFLOW);
            }
            //远程获取商品信息
            SkuInfo data = skuDetailFeignClient.getSkuInfo(skuId).getData();
//            Result<BigDecimal> price = skuDetailFeignClient.get1010SkuPrice(skuId);
            //转为购物车中的信息
            CartInfo item = converSkuInfo2CartInfo(data);
//            item.setSkuPrice(price.getData());
            item.setSkuNum(num);

            //保存到redis
            cart.put(skuId.toString(), Jsons.toStr(item));
            return  data;
        }else{
            // 之前添加过,修改商品的数量
            //获取实时价格
            BigDecimal price = skuDetailFeignClient.get1010SkuPrice(skuId).getData();
            //获取原来信息
            CartInfo cartInfo =  getItemFromCart(cartKey,skuId);
            //更新商品信息
            cartInfo.setSkuPrice(price);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
            cartInfo.setUpdateTime(new Date());

            //同步到redis
            cart.put(skuId.toString() ,Jsons.toStr(cartInfo));
            SkuInfo skuInfo = converCartInfo2SkuInfo(cartInfo);
            return skuInfo;
        }
    }

    /**
     * 获取购物车中所有的商品,排序
     * @param cartKey
     * @return
     */
    @Override
    public List<CartInfo> getCartList(String cartKey) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        List<CartInfo> infos = hashOps.values().stream()
                .map(str -> Jsons.toObj(str, CartInfo.class))
                .sorted(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())))
                .collect(Collectors.toList());

        //商品的价格再次查询更新
        //异步
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        executor.submit(()->{
            RequestContextHolder.setRequestAttributes(requestAttributes);
            updateCartAllItemsPrice(cartKey,infos);
            //移除数据
            RequestContextHolder.resetRequestAttributes();
        });
        return infos;
    }

    /**
     * 查询商品价格,同步
     * @param cartKey
     * @param infos
     */
    @Override
    public void updateCartAllItemsPrice(String cartKey, List<CartInfo> infos) {
        //1、拿到购物车
        BoundHashOperations<String, String, String> cartOps = redisTemplate.boundHashOps(cartKey);
        infos.stream()
                .forEach(cartInfo -> {
                    //1.查出最新价格
                    Result<BigDecimal> skuPrice = skuDetailFeignClient.get1010SkuPrice(cartInfo.getSkuId());
                    //更新价格
                    cartInfo.setSkuPrice(skuPrice.getData());
                    cartInfo.setUpdateTime(new Date());

                    cartOps.put(cartInfo.getSkuId().toString(),Jsons.toStr(cartInfo));
                });

    }

    /**
     * 修改购物车商品数量
     * @param skuId
     * @param num
     * @param cartKey
     */
    @Override
    public void updateItemNum(Long skuId, Integer num, String cartKey) {
        //1、拿到购物车
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        //2、拿到商品
        CartInfo item = getItemFromCart(cartKey, skuId);
        item.setSkuNum(item.getSkuNum() + num);
        item.setUpdateTime(new Date());

        //3、保存到购物车
        hashOps.put(skuId.toString(), Jsons.toStr(item));
    }

    /**
     * 修改商品的选中状态
     * @param skuId
     * @param status
     * @param cartKey
     */
    @Override
    public void updateChecked(Long skuId, Integer status, String cartKey) {

        //1、拿到购物车
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        //2、拿到商品
        CartInfo item = getItemFromCart(cartKey, skuId);
        item.setIsChecked(status);
        item.setUpdateTime(new Date());

        //3、保存到购物车
        hashOps.put(skuId.toString(), Jsons.toStr(item));
    }

    /**
     * 删除购物车里选中的商品
     * @param skuId
     * @param cartKey
     */
    @Override
    public void deleteCartItem(Long skuId, String cartKey) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);
        hashOps.delete(skuId.toString());
    }

    /**
     * 删除购物车中选中的商品
     * @param cartKey
     */
    @Override
    public void deleteChecked(String cartKey) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        //拿到选中的商品
         List<String> ids  = getCheckedItems(cartKey).stream()
                .map(cartInfo ->cartInfo.getSkuId().toString())
                 .collect(Collectors.toList());
            if (ids !=null && ids.size() >0){
                hashOps.delete(ids.toArray());
            }
    }

    /**
     * 获取购物车中选中的商品集合
     * @param cartKey
     * @return
     */
    @Override
    public List<CartInfo> getCheckedItems(String cartKey) {
        List<CartInfo> cartList = getCartList(cartKey);
        List<CartInfo> checkedItems = cartList.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .collect(Collectors.toList());
        return checkedItems;
    }

    /**
     * 合并购物车
     */
    @Override
    public void mergeUserAndTempCart() {
        UserAuthInfo authInfo = AuthUtils.getCurrentAuthInfo();
        //判断是否要合并
        if (authInfo.getUserId() !=null && !StringUtils.isEmpty(authInfo.getUserTempId())){
            //合并
            String tempCartKey = SysRedisConst.CART_KEY + authInfo.getUserTempId();
            //获取临时购物车里的所有商品
            List<CartInfo> tempCartList = getCartList(tempCartKey);
            if (tempCartList != null && tempCartList.size() >0){
                //临时购物车有商品,合拼
                String userCartKey = SysRedisConst.CART_KEY + authInfo.getUserId();
                for (CartInfo info : tempCartList){
                    Long skuId = info.getSkuId();
                    Integer skuNum = info.getSkuNum();
                    addItemToCart(skuId,skuNum,userCartKey);
                    //合并后删除一个
                    redisTemplate.opsForHash().delete(tempCartKey,skuId.toString());
                }

            }

        }

    }

    /**
     * cartInfo转换为skuInfo
     * @param cartInfo
     * @return
     */
    private SkuInfo converCartInfo2SkuInfo(CartInfo cartInfo) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuName(cartInfo.getSkuName());
        skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());
        skuInfo.setId(cartInfo.getSkuId());
        return skuInfo;
    }


    /**
     * 获取原来的信息
     * @param cartKey
     * @param skuId
     * @return
     */
    private CartInfo getItemFromCart(String cartKey, Long skuId) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(cartKey);
        //1、拿到购物车中指定商品的json数据
        String jsonData = ops.get(skuId.toString());
        return Jsons.toObj(jsonData, CartInfo.class);
    }



    /**
     * 商品信息转换为购物车要的
     * @param data
     * @return
     */
    private CartInfo converSkuInfo2CartInfo(SkuInfo data) {

        CartInfo cartInfo= new CartInfo();

        cartInfo.setSkuId(data.getId());
        cartInfo.setImgUrl(data.getSkuDefaultImg());
        cartInfo.setSkuName(data.getSkuName());
        cartInfo.setIsChecked(1);
        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());
        cartInfo.setSkuPrice(data.getPrice());
        cartInfo.setCartPrice(data.getPrice());

        return cartInfo;
    }

    /**
     * 根据用户信息决定用那个购物键
     * @return
     */
    @Override
    public String determinCartKey() {
        UserAuthInfo info = AuthUtils.getCurrentAuthInfo();
        String cartKey = SysRedisConst.CART_KEY;

        if (info.getUserId()!= null ){
            //用户登录了
            cartKey = cartKey+""+info.getUserId();
        }else {
            //用临时id
            cartKey = cartKey+""+info.getUserTempId();
        }
       return  cartKey;
    }
}
