package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface SkuDetailFeignClient {

//    @GetMapping("/api/inner/rpc/product/skudetail/{skuId}")
//    Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);
    /**
     * 查询sku的三级分类
     * @param c3Id
     * @return
     */
    @GetMapping("/skudetail/categoryView/{c3Id}")
    Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id);

    /**
     * 查询skuInfo
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/info/{skuId}")
   Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);


    /**
     * 查询sku的图片列表
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/images/{skuId}")
    Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId);


    /**
     * 查询sku的实时价格
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/price/{skuId}")
    Result<BigDecimal> get1010SkuPrice(@PathVariable("skuId") Long skuId);

    /**
     * 查询sku的销售属性名和值,标记当前的销售属性
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/saleattrvalues/{skuId}/{spuId}")
    Result<List<SpuSaleAttr>> getSkuSaleattrvalues(@PathVariable("skuId") Long skuId,
                                                          @PathVariable("spuId") Long spuId);

    /**
     * 查询sku组合,valueJson
     * @param spuId
     * @return
     */
    @GetMapping("/skudetail/valueJson/{spuId}")
    Result<String> getSkuvalueJson(@PathVariable("spuId") Long spuId);

}
