package com.atguigu.gmall.product.service.impl;



import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.starter.cache.annotation.GmallCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_category2(二级分类表)】的数据库操作Service实现
* @createDate 2022-08-23 11:56:30
*/
@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
    implements BaseCategory2Service{

    //1.使用@Resource
    //2.降低idea的warn
    //3.Mapper类上加上@Mapper注解
    @Autowired
    BaseCategory2Mapper baseCategory2Mapper;


    @Override
    public List<BaseCategory2> getCategory1Child(Long c1Id) {
      // SELECT * FROM base_category2WHERE category1_id=1

        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id",c1Id);
        List<BaseCategory2> list = baseCategory2Mapper.selectList(wrapper);
        return list;
    }
    //查询返回所有分类已经他的子分类
    @GmallCache(cacheKey = SysRedisConst.CACHE_CATEGORYS)
    @Override
    public List<CategoryTreeTo> getAllCategoryWithTree() {
       List<CategoryTreeTo> categoryTreeTos= baseCategory2Mapper.getAllCategoryWithTree();
        return categoryTreeTos;
    }
}




