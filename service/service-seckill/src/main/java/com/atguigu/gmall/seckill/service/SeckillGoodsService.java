package com.atguigu.gmall.seckill.service;

import com.atguigu.gmall.model.activity.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【seckill_goods】的数据库操作Service
* @createDate 2022-09-21 18:41:15
*/
public interface SeckillGoodsService extends IService<SeckillGoods> {

    List<SeckillGoods> getCurrentDaySeckillGoodsCache();

    List<SeckillGoods> getCurrentDaySeckillGoodsList();

}
