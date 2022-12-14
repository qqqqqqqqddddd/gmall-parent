package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author niuzepeng
* @description 针对表【base_category3(三级分类表)】的数据库操作Mapper
* @createDate 2022-08-23 11:56:30
* @Entity com.atguigu.gmall.product.domain.BaseCategory3
*/
@Mapper
public interface BaseCategory3Mapper extends BaseMapper<BaseCategory3> {
    /*
     * @description:根据三级分类的id查询二级,一级父类的信息
     * @author: niuzp
     * @date: 2022/8/27 16:04
     **/
    CategoryViewTo getCategoryView(Long category3Id);
}




