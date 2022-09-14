package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationSupport;
import java.util.List;

/**
 * 处理前端的 ajax 请求
 */
@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    CartService cartService;

    /**
     * 购物车列表
     * @return
     */
    @GetMapping("/cartList")
    public Result  cartList(){
        //决定是那个购物车
        String cartKey = cartService.determinCartKey();

        //合并购物车
        cartService.mergeUserAndTempCart();

        //获取购物车中所有的商品
        List<CartInfo> infos= cartService.getCartList(cartKey);
       return  Result.ok(infos);
    }

    /**
     * 修改购物车中某个商品数量
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result updateItemNum(@PathVariable("skuId") Long skuId,
                                @PathVariable("num") Integer num){

        String cartKey = cartService.determinCartKey();
        cartService.updateItemNum(skuId,num,cartKey);
        return Result.ok();
    }

    /**
     * 修改购物车中商品的状态
     * @param skuId
     * @param
     * @return
     */
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result check(@PathVariable("skuId") Long skuId,
                         @PathVariable("status") Integer status){

        String cartKey = cartService.determinCartKey();
        cartService.updateChecked(skuId,status,cartKey);
        return Result.ok();
    }

    //api/cart/deleteCart/56
    /**
     * 删除购物车中的商品
     * @param skuId
     * @param
     * @return
     */
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteItem(@PathVariable("skuId") Long skuId){

        String cartKey = cartService.determinCartKey();
        cartService.deleteCartItem(skuId,cartKey);
        return Result.ok();
    }
}
