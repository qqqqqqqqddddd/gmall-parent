package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetaiApiController {

    @Autowired
    BaseCategory3Service baseCategory3Service;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

//  @GetMapping("/skudetail/{skuId}")
//  public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
//
//      SkuDetailTo skuDetailTo=skuInfoService.getSkuDetail(skuId);
//      return  Result.ok(skuDetailTo);
//  }

    /**
     * 查询sku的三级分类
     * @param c3Id
     * @return
     */
    @GetMapping("/skudetail/categoryView/{c3Id}")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id){
        CategoryViewTo categoryViewTo= baseCategory3Service.getCategoryView(c3Id);
        return Result.ok(categoryViewTo);
    }

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

    /**
     * 查询sku的实时价格
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/price/{skuId}")
    public Result<BigDecimal> get1010SkuPrice(@PathVariable("skuId") Long skuId){
        BigDecimal realPrice=  skuInfoService.getDetailSkuPrice(skuId);
        return Result.ok(realPrice);
    }

    /**
     * 查询sku的销售属性名和值,标记当前的销售属性
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/saleattrvalues/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSkuSaleattrvalues(@PathVariable("skuId") Long skuId,
                                         @PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> saleAttrsList =spuSaleAttrService.getSaleAttrAndValueMarkSku(spuId,skuId);
        return Result.ok(saleAttrsList);
    }

    /**
     * 查询sku组合,valueJson
     * @param spuId
     * @return
     */
    @GetMapping("/skudetail/valueJson/{spuId}")
    public Result<String> getSkuvalueJson(@PathVariable("spuId") Long spuId){
        String valuejson= spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        return Result.ok(valuejson);
    }



}
