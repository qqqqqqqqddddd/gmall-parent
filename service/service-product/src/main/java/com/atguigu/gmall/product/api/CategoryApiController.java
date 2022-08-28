package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台的分类api
 */
@Api(tags = "三级分类RPC接口")
@RestController
@RequestMapping("/api/inner/rpc/product")
public class CategoryApiController {


    @Autowired
    BaseCategory2Service baseCategory2Service;

    @ApiOperation("三级分类树形结构查询")
    @GetMapping("/category/tree")
    public Result  getAllCategoryWithTree(){
        List<CategoryTreeTo> categoryTreeTos= baseCategory2Service.getAllCategoryWithTree();
        return  Result.ok(categoryTreeTos);
    }

}
