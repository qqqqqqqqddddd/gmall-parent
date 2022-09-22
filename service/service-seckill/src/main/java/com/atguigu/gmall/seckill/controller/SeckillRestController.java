package com.atguigu.gmall.seckill.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.seckill.biz.SeckillBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity/seckill/auth")
public class SeckillRestController {

    @Autowired
    SeckillBizService bizService;

    /**
     * 生成秒杀码，以便于真实隐藏秒杀地址
     * @param skuId
     * @return
     */
    @GetMapping("/getSeckillSkuIdStr/{skuId}")
    public Result getSeckillCode(@PathVariable("skuId") Long skuId){


        String code = bizService.generateSeckillCode(skuId);
        return Result.ok(code);
    }


    /**
     * 秒杀预下单
     * @param skuId
     * @return
     */
    @PostMapping("/seckillOrder/{skuId}")
    public Result seckillOrder(@PathVariable("skuId") Long skuId,
                               @RequestParam("skuIdStr") String skuIdStr){

        ResultCodeEnum codeEnum = bizService.seckillOrder(skuId, skuIdStr);
        //秒杀码是否合法
        //开始秒杀
        //返回结果

        return Result.build("",codeEnum);//响应成功 200 。
    }

    /**
     * 检查秒杀订单的状态
     */
    @GetMapping("/checkOrder/{skuId}")
    public Result checkOrder(@PathVariable("skuId") Long skuId){

        ResultCodeEnum resultCodeEnum = bizService.checkSeckillOrderStatus(skuId);
        return Result.build("",resultCodeEnum);
    }

    @PostMapping("/submitOrder")
    public  Result submitOrder(@RequestBody OrderInfo orderInfo){
        Long orderId = bizService.submitSeckillOrder(orderInfo);
        //响应订单id
        return Result.ok(orderId.toString());
    }



}
