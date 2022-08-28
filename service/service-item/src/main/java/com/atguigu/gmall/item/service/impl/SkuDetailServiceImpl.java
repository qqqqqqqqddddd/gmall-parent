package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        skuDetailTo.setSkuInfo();

        //2.查询图片信息
//        skuDetailTo.set

         //查询实时价格
        skuDetailTo.setPrice();

        //查询销售属性名和值
        skuDetailTo.setSpuSaleAttrList();

        //查询所有的销售属性组合
        skuDetailTo.setValuesSkuJson();

        //查询分类
        skuDetailTo.setCategoryView();

        return skuDetailTo;
    }
}
