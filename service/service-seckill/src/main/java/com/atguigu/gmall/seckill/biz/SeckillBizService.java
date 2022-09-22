package com.atguigu.gmall.seckill.biz;

import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.seckill.SeckillOrderConfirmVo;

public interface SeckillBizService {

    String generateSeckillCode(Long skuId);

    boolean checkSeckillCode(Long skuId,String code);

    ResultCodeEnum seckillOrder(Long skuId, String skuIdStr);

    ResultCodeEnum checkSeckillOrderStatus(Long skuId);

    Long submitSeckillOrder(OrderInfo orderInfo);

    SeckillOrderConfirmVo getSeckillOrderConfirmVo(Long skuId);
}
