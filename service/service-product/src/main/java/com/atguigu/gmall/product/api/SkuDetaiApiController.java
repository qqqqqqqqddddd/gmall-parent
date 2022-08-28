package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetaiApiController {

    @Autowired
    SkuInfoService skuInfoService;

//  @GetMapping("/skudetail/{skuId}")
//  public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
//
//      SkuDetailTo skuDetailTo=skuInfoService.getSkuDetail(skuId);
//      return  Result.ok(skuDetailTo);
//  }

    /**
     * 查询skuInfo
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/info/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId){
      SkuInfo skuInfo=  skuInfoService.getDetailSkuInfo(skuId);
       return Result.ok(skuInfo);
    }


    /**
     * 查询sku的图片列表
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId){
        List<SkuImage> images=  skuInfoService.getDetailSkuImages(skuId);
        return Result.ok(images);
    }






}
