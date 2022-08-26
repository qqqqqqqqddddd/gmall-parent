package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "spu")
@RestController
@RequestMapping("/admin/product/")
public class SpuController {


    @Autowired
    SpuInfoService  spuInfoService;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;



    /*
     * @description:查询分页spu
     * @author: niuzp
     * @date: 2022/8/26 21:33
     **/
    @GetMapping("/{pn}/{ps}")
    public Result getSpuPage(@PathVariable("pn") Integer pn,
                             @PathVariable("ps") Integer ps,
                             @RequestParam("category3Id") Long category3Id){

        Page<SpuInfo> page = new Page<>(pn,ps);
        QueryWrapper<SpuInfo> wrapper= new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);
        //分页数据集合
        Page<SpuInfo> result = spuInfoService.page(page, wrapper);
        return Result.ok(result);
    }


    /*
     * @description: 保存spu
     * @author: niuzp
     * @date: 2022/8/26 21:40
     **/
    @PostMapping("/saveSpuInfo")
    public  Result  saveSpuInfo(@RequestBody SpuInfo info){

        //保存到spu_image spu_info spu_sale_attr spu_sale_attr_value
        spuInfoService.saveSpuInfo(info);
        return  Result.ok();
   }

   /*
    * @description:查询spu图片
    * @author: niuzp
    * @date: 2022/8/26 22:47
    **/
   @GetMapping("/spuImageList/{spuId}")
   public Result getSpuImageList(@PathVariable("spuId") Long spuId){

       QueryWrapper<SpuImage> wrapper= new QueryWrapper<>();
       wrapper.eq("spu_id",spuId);
       List<SpuImage> list = spuImageService.list(wrapper);
       return Result.ok(list);
   }

    /*
     * @description:查询spu销售属性
     * @author: niuzp
     * @date: 2022/8/26 22:47
     **/
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId){
       List<SpuSaleAttr> list= spuSaleAttrService.getSaleAttrAndValueBySpuId(spuId);
        return Result.ok(list);
    }

}
