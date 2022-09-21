package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.seckill.SeckillFeignClient;
import com.atguigu.gmall.model.activity.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.rmi.MarshalledObject;
import java.util.List;

@Controller
public class SeckillController {


    @Autowired
    SeckillFeignClient seckillFeignClient;


    /**
     * 来到秒杀页
     * @return
     */
    @GetMapping("/seckill.html")
     public String seckillPage(Model model){
        //查询秒杀的数据
        //{skuId、skuDefaultImg、skuName、price、costPrice、num、stockCount}
        Result<List<SeckillGoods>> goods = seckillFeignClient.getCurrentDaySeckillGoodsList();
        model.addAttribute("list",goods.getData());
         return  "seckill/index";
     }






}
