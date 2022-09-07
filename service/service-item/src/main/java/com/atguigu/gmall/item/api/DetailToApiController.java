package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品信息RPC接口")
@RestController
@RequestMapping("/api/inner/rpc/item")
public class DetailToApiController {


    @Autowired
    SkuDetailService skuDetailService;

    @Autowired
    SearchFeignClient searchFeignClient;



    @GetMapping("/skudetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
      //商品的详细信息
      SkuDetailTo skuDetailTo=skuDetailService.getSkuDetail(skuId);

      //更新商品的热度 攒一批更一下
       skuDetailService.updateScore(skuId);
       return  Result.ok(skuDetailTo);
    }



}
