package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

     @Autowired
     SkuDetailFeignClient skuDetailFeignClient;

     @Autowired
    ThreadPoolExecutor executor;

     //本地缓存: 缺点: 容量问题, 同步问题
    // private Map<Long,SkuDetailTo> skuCache = new ConcurrentHashMap<>();

    //大量数据的缓存  分布式缓存
    @Autowired
    StringRedisTemplate redisTemplate;



    //未引入缓存优化前
    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
//        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);

        //异步编排:
        //CompletableFuture
        //CompletableFuture.runAsync()  //启动以后不用返回结果的
        //CompletableFuture.supplyAsync() //启动以后要有返回结果的

//        优化,分接口查询
        //1.查询基本信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            skuDetailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);


        //2.查询图片信息
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            List<SkuImage> images = skuImages.getData();
            skuInfo.setSkuImageList(images);
        }), executor);


        //查询实时价格
        CompletableFuture<Void> skuPriceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> skuPrice = skuDetailFeignClient.get1010SkuPrice(skuId);
            BigDecimal price = skuPrice.getData();
            skuDetailTo.setPrice(price);
        }, executor);

        //查询销售属性名和值
        CompletableFuture<Void> spuSaleAttrFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
            Result<List<SpuSaleAttr>> skuSaleattrvalues = skuDetailFeignClient.getSkuSaleattrvalues(skuId, skuInfo.getSpuId());
            List<SpuSaleAttr> spuSaleAttrs = skuSaleattrvalues.getData();
            skuDetailTo.setSpuSaleAttrList(spuSaleAttrs);
        }), executor);


        //查询所有的销售属性组合
        CompletableFuture<Void> skuvalueJsonFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
            Result<String> skuvalueJson = skuDetailFeignClient.getSkuvalueJson(skuInfo.getSpuId());
            String svalueJson = skuvalueJson.getData();
            skuDetailTo.setValuesSkuJson(svalueJson);
        }), executor);

        //查询分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            CategoryViewTo categoryViewTo = categoryView.getData();
            skuDetailTo.setCategoryView(categoryViewTo);
        }), executor);

        CompletableFuture
                .allOf(skuInfoFuture,imageFuture,skuPriceFuture,spuSaleAttrFuture,skuvalueJsonFuture,categoryViewFuture)
                .join();
        return skuDetailTo;
    }



    //进行本地缓存优化
//    @Override
//    public SkuDetailTo getSkuDetail(Long skuId) {
//
//        //1.先看缓存
//        SkuDetailTo cacheData = skuCache.get(skuId);
//        //2.是否命中
//        if ( cacheData == null){
//            //缓存没有,查询出,回源
//            //提示缓存命中率 : 预缓存机制
//            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
//            //放入缓存
//            skuCache.put(skuId,fromRpc);
//            return  fromRpc;
//        }
//        return  cacheData;
//
//    }

    //进行分布式缓存优化
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

        String jsonStr = redisTemplate.opsForValue().get("sku:info:"+skuId);
        if ("x".equals(jsonStr)){
            //说明以前查过,数据库没有记录
            return  null;
        }
        if (StringUtils.isEmpty(jsonStr)){
            //缓存中没有 回源 放入缓存
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            String cacheJson="x";
            if (fromRpc!=null){
                cacheJson = Jsons.toStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:"+skuId,cacheJson,7, TimeUnit.DAYS);
            }else {
                redisTemplate.opsForValue().set("sku:info:"+skuId,cacheJson,30,TimeUnit.MINUTES);
            }
            return  fromRpc;
        }
        //缓存中有数据
        SkuDetailTo skuDetailTo = Jsons.toObj(jsonStr, SkuDetailTo.class);
        return skuDetailTo;
    }
}
