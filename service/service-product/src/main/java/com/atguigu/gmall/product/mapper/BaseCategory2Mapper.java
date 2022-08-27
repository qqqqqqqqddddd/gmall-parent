package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_category2(二级分类表)】的数据库操作Mapper
* @createDate 2022-08-23 11:56:30
* @Entity com.atguigu.gmall.product.domain.BaseCategory2
*/
@Mapper
public interface BaseCategory2Mapper extends BaseMapper<BaseCategory2> {

    List<CategoryTreeTo> getAllCategoryWithTree();

}




