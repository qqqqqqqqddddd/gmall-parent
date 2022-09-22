package com.atguigu.gmall.seckill.service;

import com.atguigu.gmall.model.activity.SeckillGoods;

import java.util.List;

public interface SeckillGoodsCacheOpsService {
    List<SeckillGoods> getSeckillGoodsFromLocal();


    void syncLocalAndRedisCache();

    List<SeckillGoods> getSeckillGoodsFromRemote();

    void upSeckillGoods(List<SeckillGoods> list);

    void clearCache();

    SeckillGoods getSeckillGoodsDetail(Long skuId);
}
