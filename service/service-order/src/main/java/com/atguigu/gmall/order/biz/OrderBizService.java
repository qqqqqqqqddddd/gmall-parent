package com.atguigu.gmall.order.biz;

import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.order.OrderWareMapVo;
import com.atguigu.gmall.model.vo.order.WareChildOrderVo;

import java.util.List;

/**
 * 订单业务
 */

public interface OrderBizService {


    OrderConfirmDataVo getOrderConfirmData();


    String generateTradeNo();


    boolean checkTradeNo(String tradeNo);


    Long submitOrder(OrderSubmitVo submitVo, String tradeNo);

    void closeOrder(Long orderId, Long userId);

    List<WareChildOrderVo> orderSplit(OrderWareMapVo vo);
}
