package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
* @createDate 2022-08-25 09:14:48
* @Entity com.atguigu.gmall.product.domain.BaseAttrInfo
*/
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {


    List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(@Param("c1Id") long c1Id,
                                                       @Param("c2Id") long c2Id,
                                                       @Param("c3Id") long c3Id);



}




