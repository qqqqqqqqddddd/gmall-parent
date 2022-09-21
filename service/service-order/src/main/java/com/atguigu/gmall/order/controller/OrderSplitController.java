package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderWareMapVo;
import com.atguigu.gmall.model.vo.order.WareChildOrderVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 拆单
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderSplitController {


    @Autowired
    OrderBizService orderBizService;

    /**
     * 拆单
     * @param vo
     * @return
     */
    @PostMapping("/orderSplit")
    public List<WareChildOrderVo> orderSplit(OrderWareMapVo vo) {
    log.info("订单执行拆单:{}",vo);


      return orderBizService.orderSplit(vo);
    }


}
