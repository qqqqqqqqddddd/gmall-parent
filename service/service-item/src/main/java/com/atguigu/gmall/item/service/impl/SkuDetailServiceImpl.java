package com.atguigu.gmall.item.service.impl;


import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.starter.cache.annotation.GmallCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

     @Autowired
     SkuDetailFeignClient skuDetailFeignClient;

     @Autowired
    ThreadPoolExecutor executor;


    ConcurrentHashMap<Long,ReentrantLock> lockPool =  new ConcurrentHashMap(); //锁池中

    ReentrantLock lock =  new ReentrantLock(); //锁的粒度太大把无关的都锁住了

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
          if(skuInfo != null){
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            List<SkuImage> images = skuImages.getData();
            skuInfo.setSkuImageList(images);
          }
        }), executor);


        //查询实时价格
        CompletableFuture<Void> skuPriceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> skuPrice = skuDetailFeignClient.get1010SkuPrice(skuId);
            BigDecimal price = skuPrice.getData();
            skuDetailTo.setPrice(price);
        }, executor);

        //查询销售属性名和值
        CompletableFuture<Void> spuSaleAttrFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
          if(skuInfo != null){
            Result<List<SpuSaleAttr>> skuSaleattrvalues = skuDetailFeignClient.getSkuSaleattrvalues(skuId, skuInfo.getSpuId());
            List<SpuSaleAttr> spuSaleAttrs = skuSaleattrvalues.getData();
            skuDetailTo.setSpuSaleAttrList(spuSaleAttrs);
                    }
        }), executor);


        //查询所有的销售属性组合
        CompletableFuture<Void> skuvalueJsonFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
           if(skuInfo != null){
            Result<String> skuvalueJson = skuDetailFeignClient.getSkuvalueJson(skuInfo.getSpuId());
            String svalueJson = skuvalueJson.getData();
            skuDetailTo.setValuesSkuJson(svalueJson);
            }
        }), executor);

        //查询分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync((skuInfo -> {
           if(skuInfo != null) {
               Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
               CategoryViewTo categoryViewTo = categoryView.getData();
               skuDetailTo.setCategoryView(categoryViewTo);
           }
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
    //@SneakyThrows
    public SkuDetailTo getSkuDetailxxxx(Long skuId) throws InterruptedException {
           //往锁池中放入每个商品的锁
       lockPool.put(skuId,new ReentrantLock());

        String jsonStr = redisTemplate.opsForValue().get("sku:info:"+skuId);
        if ("x".equals(jsonStr)){
            //说明以前查过,数据库没有记录
            return  null;
        }
        if (StringUtils.isEmpty(jsonStr)){

            SkuDetailTo fromRpc = null;
            //先判断锁池中是否有锁 ,有就用没有就放一把锁
            ReentrantLock lock = lockPool.putIfAbsent(skuId, new ReentrantLock());
//            ReentrantLock reentrantLock = lockPool.get(skuId);
            boolean b = this.lock.tryLock(); //瞬发锁,等不到直接走

             if (b){
                 fromRpc = getSkuDetailFromRpc(skuId);
             }else {
                 Thread.sleep(1000);
             }
            //缓存中没有 回源 放入缓存

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

    @GmallCache(
            cacheKey = SysRedisConst.SKU_INFO_PREFIX+"#{#params[0]}",
            bloomName = SysRedisConst.BLOOM_SKUID,
            bloomValue = "#{#params[0]}",
            lockName = SysRedisConst.LOCK_SKU_DETAIL+"#{#params[0]}"
    )
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
        return fromRpc;
    }
}
