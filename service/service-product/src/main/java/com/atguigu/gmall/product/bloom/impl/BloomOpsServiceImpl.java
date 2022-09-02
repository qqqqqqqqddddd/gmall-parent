package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloomOpsServiceImpl implements BloomOpsService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuInfoService skuInfoService;

    @Override
    public void rebuildBloom(String bloomName, BloomDataQueryService dataQueryService) {
        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(bloomName);
        //创建一个新的布隆
        String newBloomName=bloomName+"_new";
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(newBloomName);

        //初始化布隆
        //List<Long> skuIds  = skuInfoService.findAllSkuId();
        List list = dataQueryService.queryData(); //动态决定

         bloomFilter.tryInit(5000000,0.00001);
        for (Object skuId : list) {
            bloomFilter.add(skuId);
        }

        //交换
        oldBloomFilter.rename("aaaaa_bloom");
        bloomFilter.rename(bloomName);

         //删除老布隆和中间层
        oldBloomFilter.deleteAsync();
        redissonClient.getBloomFilter("aaaaa_bloom").deleteAsync();
    }
}
