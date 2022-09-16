package com.atguigu.gmall.pay.controller;


import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.pay.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/payment")
public class PayController {


    @Autowired
    AlipayService  alipayService;

    /**
     * 跳转到支付宝的收银台
     * @param orderId
     * @return
     */
    @ResponseBody
    @GetMapping("/alipay/submit/{orderId}")
    public String  alipayPage(@PathVariable("orderId") Long orderId) throws AlipayApiException {

       String content = alipayService.getAlipayPageHtml(orderId);

        return  content;
    }

    /**
     * 支付成功页
     *
     */
    @GetMapping("/success.html")
    public  String paySuccessPage(@RequestParam Map<String,String> paramMaps) throws AlipayApiException {
        System.out.println("同步通知,收到的参数:"+paramMaps);
        // 修改订单状态,先验签
        boolean b = alipayService.rsaCheckV1(paramMaps);

        if (b){
            //通过
            System.out.println("正在修改订单状态,,订单详细"+paramMaps);
        }

        return  "redirect:http://gmall.com/pay/success.html";
    }


    @PostMapping("/success/notify")
    public String  successNotify(@RequestParam Map<String,String> map) throws AlipayApiException {
        boolean b = alipayService.rsaCheckV1(map);
        if(b){
            log.info("异步通知抵达。支付成功，验签通过。数据：{}", Jsons.toStr(map));

        }else {
            return "error";
        }
        return "success";
    }

}
