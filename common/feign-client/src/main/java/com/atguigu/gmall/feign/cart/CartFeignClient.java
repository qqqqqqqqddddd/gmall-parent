package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {


    @GetMapping("/addToCart")
    Result<Object> addToCart(@RequestParam("skuId") Long skuId,
                     @RequestParam("num") Integer num);


    /**
     * 删除购物车中选中的商品
     * @return
     */
    @DeleteMapping("/deleteChecked")
    Result deleteChecked();



}
