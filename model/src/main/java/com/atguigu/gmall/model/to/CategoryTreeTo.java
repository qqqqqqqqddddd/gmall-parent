package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * 三级分类的树,传输对象
 *
 */
@Data
public class CategoryTreeTo {

    private  Long categoryId;

    private  String categoryName;

    private List<CategoryTreeTo> categoryChild; //子分类
}
