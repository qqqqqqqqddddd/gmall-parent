package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2022-09-14 19:31:53
*/
public interface OrderInfoService extends IService<OrderInfo> {

    Long saveOrder(OrderSubmitVo submitVo, String tradeNo);

    OrderInfo prepareOrderInfo(OrderSubmitVo submitVo, String tradeNo);

    void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected);

    OrderInfo getOrderInfoByOutTradeNoAndUserId(String outTradeNo, Long userId);

    OrderInfo getOrderInfoByOrderIdAndUserId(Long orderId, Long userId);

}
