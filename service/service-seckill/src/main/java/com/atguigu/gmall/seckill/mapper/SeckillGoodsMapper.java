package com.atguigu.gmall.seckill.mapper;


import com.atguigu.gmall.model.activity.SeckillGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【seckill_goods】的数据库操作Mapper
* @createDate 2022-09-21 18:41:15
* @Entity com.atguigu.gmall.seckill.domain.SeckillGoods
*/
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    List<SeckillGoods> getSeckillGoodsByDate(@Param("date") String date);

    void updateStockCount(@Param("skuId") Long skuId);
}




