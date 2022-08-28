package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

     @Autowired
     SkuDetailFeignClient skuDetailFeignClient;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
//        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);

//        优化,分接口查询
        //1.查询基本信息
        Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
        SkuInfo skuInfo = result.getData();
        skuDetailTo.setSkuInfo(skuInfo);


        //2.查询图片信息
        Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
        List<SkuImage> images = skuImages.getData();
        skuInfo.setSkuImageList(images);


        //查询实时价格
        Result<BigDecimal> skuPrice = skuDetailFeignClient.get1010SkuPrice(skuId);
        BigDecimal price = skuPrice.getData();
        skuDetailTo.setPrice(price);

        //查询销售属性名和值
        Result<List<SpuSaleAttr>> skuSaleattrvalues = skuDetailFeignClient.getSkuSaleattrvalues(skuId, skuInfo.getSpuId());
        List<SpuSaleAttr> spuSaleAttrs = skuSaleattrvalues.getData();
        skuDetailTo.setSpuSaleAttrList(spuSaleAttrs);

        //查询所有的销售属性组合
        Result<String> skuvalueJson = skuDetailFeignClient.getSkuvalueJson(skuInfo.getSpuId());
        String svalueJson = skuvalueJson.getData();
        skuDetailTo.setValuesSkuJson(svalueJson);

        //查询分类
        Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
        CategoryViewTo categoryViewTo = categoryView.getData();
        skuDetailTo.setCategoryView(categoryViewTo);

        return skuDetailTo;
    }
}
