package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {


    @GetMapping("/addToCart")
    public Result addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            @RequestHeader(value = SysRedisConst.USERID_HEADER,required = false) String userId){

        System.out.println("service-cart 获取到用户id :" + userId);
        //TODO

        return Result.ok();
    }


}
