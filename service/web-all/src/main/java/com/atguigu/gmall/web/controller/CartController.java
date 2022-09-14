package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {


    @Autowired
    CartFeignClient cartFeignClient;


    /**
     * 添加商品到购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/addCart.html")
    public  String addCarthtml(@RequestParam("skuId") Long skuId,
                               @RequestParam("skuNum") Integer skuNum,
                               Model model){
        //商品添加到购物车
        System.out.println("web-all 获取用户的id:");
        Result<Object> result = cartFeignClient.addToCart(skuId, skuNum);
        if (result.isOk()) {
            model.addAttribute("skuInfo",result.getData());
            model.addAttribute("skuNum",skuNum);
            return "cart/addCart";
        }else {
            String message = result.getMessage();
            model.addAttribute("msg",result.getData());
            return "cart/error";
        }
    }

    /**
     * 跳到购物车列表页
     * @param
     * @return
     */
    @GetMapping("/cart.html")
    public  String cartHtml(){


        return "cart/index";
    }


    /**
     * 删除购物车中选中的商品
     * @param
     * @return
     */
    @GetMapping("/cart/deleteChecked")
    public  String deleteChecked(){

        cartFeignClient.deleteChecked();
        return "redirect:http://cart.gmall.com/cart.html";
    }

}
