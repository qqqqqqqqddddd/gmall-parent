package com.atguigu.gmall.order.service.impl;
import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.constant.MqConst;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.to.mq.OrderMsg;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.google.common.collect.Lists;
import com.atguigu.gmall.model.activity.CouponInfo;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niuzepeng
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
 * @createDate 2022-09-14 19:31:53
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 保存订单基本信息
     *
     * @param submitVo
     * @param tradeNo
     * @return
     */
    @Transactional
    @Override
    public Long saveOrder(OrderSubmitVo submitVo, String tradeNo) {
        //生成orderInfo
        OrderInfo orderInfo = prepareOrderInfo(submitVo,tradeNo);

        //保存-orderInfo
        orderInfoMapper.insert(orderInfo);
        //保存-orderDetail
        List<OrderDetail> details = prepareOrderDetail(submitVo,orderInfo);
        orderDetailService.saveBatch(details);

        //发送订单创建完成消息
        OrderMsg orderMsg = new OrderMsg(orderInfo.getId(),orderInfo.getUserId());
        rabbitTemplate.convertAndSend(
                MqConst.EXCHANGE_ORDER_EVNT,
                MqConst.RK_ORDER_CREATED,
                Jsons.toStr(orderMsg)
        );

        return orderInfo.getId();
    }

    /**
     * 生成订单明细
     * @param submitVo
     * @param orderInfo
     * @return
     */
    private List<OrderDetail> prepareOrderDetail(OrderSubmitVo submitVo, OrderInfo orderInfo) {
        List<OrderDetail> detailList = submitVo.getOrderDetailList().stream()
                .map(vo -> {
                    OrderDetail detail = new OrderDetail();
                    //订单id
                    detail.setOrderId(orderInfo.getId());
                    //skuId
                    detail.setSkuId(vo.getSkuId());
                    //用户id
                    detail.setUserId(orderInfo.getUserId());

                    detail.setSkuName(vo.getSkuName());
                    detail.setImgUrl(vo.getImgUrl());
                    detail.setOrderPrice(vo.getOrderPrice());
                    detail.setSkuNum(vo.getSkuNum());
                    detail.setHasStock(vo.getHasStock());
                    detail.setCreateTime(new Date());
                    detail.setSplitTotalAmount(vo.getOrderPrice().multiply(new BigDecimal(vo.getSkuNum() + "")));
                    detail.setSplitActivityAmount(new BigDecimal("0"));
                    detail.setSplitCouponAmount(new BigDecimal("0"));
                    return detail;
                }).collect(Collectors.toList());
        return detailList;
    }


    /**
     * 生成订单数据
     * @param submitVo
     * @param tradeNo
     * @return
     */
    @Override
    public OrderInfo prepareOrderInfo(OrderSubmitVo submitVo, String tradeNo) {

        OrderInfo orderInfo = new OrderInfo();
        //收货人
        orderInfo.setConsignee(submitVo.getConsignee());
        orderInfo.setConsigneeTel(submitVo.getConsigneeTel());

        //用户id
        Long userId = AuthUtils.getCurrentAuthInfo().getUserId();
        orderInfo.setUserId(userId);
        //支付方式
        orderInfo.setPaymentWay(submitVo.getPaymentWay());
        orderInfo.setDeliveryAddress(submitVo.getDeliveryAddress());
        orderInfo.setOrderComment(submitVo.getOrderComment());

        orderInfo.setOutTradeNo(tradeNo);
        orderInfo.setTradeBody(submitVo.getOrderDetailList().get(0).getSkuName());
        //创建时间
        orderInfo.setCreateTime(new Date());
        orderInfo.setExpireTime(new Date(System.currentTimeMillis() + 1000* SysRedisConst.ORDER_CLOSE_TTL));
        //订单的处理状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        //物流编号
        orderInfo.setTrackingNo("");

        //父订单id
        orderInfo.setParentOrderId(0L);
        //订单图片
        orderInfo.setImgUrl(submitVo.getOrderDetailList().get(0).getImgUrl());



        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        //计算总价
        BigDecimal totalAmount = submitVo.getOrderDetailList()
                .stream()
                .map(o -> o.getOrderPrice()
                        .multiply(new BigDecimal(o.getSkuNum() + "")))
                .reduce((o1, o2) -> o1.add(o2))
                .get();
        orderInfo.setOriginalTotalAmount(totalAmount);

        orderInfo.setTotalAmount(totalAmount);
        //最后退款时间
        orderInfo.setRefundableTime(new Date(System.currentTimeMillis() + SysRedisConst.ORDER_REFUND_TTL*1000));
        //运费
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());


        return orderInfo;

    }

    /**
     * 幂等修改订单状态
     * @param orderId
     * @param userId
     * @param closed
     * @param expected
     */
    @Override
    public void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected) {
        String orderStatus = closed.getOrderStatus().name();
        String processStatus = closed.name();

        List<String> expects = expected.stream().map(status -> status.name()).collect(Collectors.toList());

        //幂等修改订单
        orderInfoMapper.updateOrderStatus(orderId,userId,processStatus,orderStatus,expects);
    }
}




