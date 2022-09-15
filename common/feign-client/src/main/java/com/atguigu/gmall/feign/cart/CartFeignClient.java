package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {

    /**
     * 添加购物车
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/addToCart")
    Result<Object> addToCart(@RequestParam("skuId") Long skuId,
                     @RequestParam("num") Integer num);


    /**
     * 删除购物车中选中的商品
     * @return
     */
    @DeleteMapping("/deleteChecked")
    Result deleteChecked();

    /**
     * 获取购物车中选中的商品
     * @return
     */
    @GetMapping("/checked/list")
    Result<List<CartInfo>> getChecked();



}
