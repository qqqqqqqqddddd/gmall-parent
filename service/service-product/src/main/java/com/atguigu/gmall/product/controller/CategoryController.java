package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 三级分类
 */
@RestController
@RequestMapping("/admin/product")
public class CategoryController {


    @Autowired
    BaseCategory1Service baseCategory1Service;

    @Autowired
    BaseCategory2Service baseCategory2Service;

    @Autowired
    BaseCategory3Service baseCategory3Service;

    @GetMapping("/getCategory1")
    public Result getCategory1(){

        //一级分类
        List<BaseCategory1> list = baseCategory1Service.list();

        return Result.ok(list);
    }


    @GetMapping("/getCategory2/{c1Id}")
    public Result getCategory2(@PathVariable("c1Id") Long c1Id){
        //一级分类对应的二级分类
        List<BaseCategory2> category2s = baseCategory2Service.getCategory1Child(c1Id);

        return Result.ok(category2s);
    }

     //admin/product/getCategory3/4
    @GetMapping("/getCategory3/{c2Id}")
    public  Result getCategory3(@PathVariable("c2Id") Long c2Id){
        //二级分类对应的三级分类
        List<BaseCategory3> category3s = baseCategory3Service.getCategory2Child(c2Id);

        return Result.ok(category3s);

    }

}
