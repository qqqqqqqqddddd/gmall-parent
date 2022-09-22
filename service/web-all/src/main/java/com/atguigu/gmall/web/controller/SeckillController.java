package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.seckill.SeckillFeignClient;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.model.vo.seckill.SeckillOrderConfirmVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 秒杀商品详情页
     * @param model
     * @param skuId
     * @return
     */
     @GetMapping("/seckill/{skuId}.html")
     public  String seckillDetail(Model model,
                                  @PathVariable("skuId") Long skuId){

         Result<SeckillGoods> seckillGood = seckillFeignClient.getSeckillGood(skuId);
         model.addAttribute("item",seckillGood.getData());

         return  "seckill/item";

     }


    /**
     * 秒杀排队页
     * @param model
     * @param skuId
     * @return
     */
    @GetMapping("/seckill/queue.html")
    public  String seckillQueue(Model model,
                                @RequestParam("skuId") Long skuId,
                                @RequestParam("skuIdStr") String skuIdStr){
        model.addAttribute("skuId",skuId);
        model.addAttribute("skuIdStr",skuIdStr);
        return  "seckill/queue";

    }

    @GetMapping("/seckill/trade.html")
    public String trade(Model model,@RequestParam("skuId") Long skuId){

        Result<SeckillOrderConfirmVo> confirmVo =
                seckillFeignClient.getSeckillOrderConfirmVo(skuId);

        SeckillOrderConfirmVo voData = confirmVo.getData();
        //返回的是订单确认页的数据
        model.addAttribute("detailArrayList",voData.getTempOrder().getOrderDetailList());
        model.addAttribute("userAddressList",voData.getUserAddressList());
        model.addAttribute("totalNum",voData.getTempOrder().getOrderDetailList().size());

        model.addAttribute("totalAmount",voData.getTempOrder().getTotalAmount());
        return "seckill/trade";
    }





}
