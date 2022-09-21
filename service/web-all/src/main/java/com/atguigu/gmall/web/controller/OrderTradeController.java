package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderTradeController {


    @Autowired
    OrderFeignClient orderFeignClient;


    /**
     * 跳转到订单确认页
     * @return
     */
    @GetMapping("/trade.html")
    public  String tradePage(Model  model){

        Result<OrderConfirmDataVo> result = orderFeignClient.getOrderConfirmData();
        if (result.isOk()) {

            OrderConfirmDataVo vo = result.getData();
            model.addAttribute("detailArrayList",vo.getDetailArrayList());
            model.addAttribute("totalNum",vo.getTotalNum());
            model.addAttribute("totalAmount",vo.getTotalAmount());
            model.addAttribute("userAddressList",vo.getUserAddressList());

            //订单的追踪号
            model.addAttribute("tradeNo",vo.getTradeNo());

        }
        return  "order/trade";
    }


    /**
     * 订单列表页
     * @return
     */
    @GetMapping("/myOrder.html")
    public String myOrderPage(){

        return  "order/myOrder";
    }



}
