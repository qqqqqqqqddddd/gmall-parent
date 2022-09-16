package com.atguigu.gmall.order.biz;

import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;

/**
 * 订单业务
 */

public interface OrderBizService {


    OrderConfirmDataVo getOrderConfirmData();


    String generateTradeNo();


    boolean checkTradeNo(String tradeNo);


    Long submitOrder(OrderSubmitVo submitVo, String tradeNo);

    void closeOrder(Long userId, Long orderId);
}
