package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SkuController {


    @Autowired
    SkuInfoService skuInfoService;
    /*
     * @description:分页查询sku
     * @author: niuzp
     * @date: 2022/8/26 22:42
     **/
    @GetMapping("/list/{pn}/{ps}")
    public Result getSpuPage(@PathVariable("pn") Integer pn,
                             @PathVariable("ps") Integer ps){

        Page<SkuInfo> page = new Page<>(pn,ps);
        //分页数据集合
        Page<SkuInfo> result = skuInfoService.page(page);
        return Result.ok(result);
    }

    /*
     * @description: sku大保存
     * @author: niuzp
     * @date: 2022/8/26 23:19
     **/
    @PostMapping("/saveSkuInfo")
    public  Result  saveSku(@RequestBody SkuInfo skuInfo){

        skuInfoService.saveSkuInfo(skuInfo);
        return  Result.ok();
    }


    /*
     * @description: 商品下架
     * @author: niuzp
     * @date: 2022/8/26 23:19
     **/
    @GetMapping("/cancelSale/{skuId}")
    public  Result cancelSale(@PathVariable("skuId") Long skuId){
        skuInfoService.cancelSale(skuId);
        return  Result.ok();
    }

    /*
     * @description: 商品上架
     * @author: niuzp
     * @date: 2022/8/26 23:19
     **/
    @GetMapping("/onSale/{skuId}")
    public  Result onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.onSale(skuId);
        return  Result.ok();
    }

}
