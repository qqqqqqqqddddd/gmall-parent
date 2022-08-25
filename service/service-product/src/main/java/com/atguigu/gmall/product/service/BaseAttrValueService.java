package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2022-08-25 09:14:48
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    List<BaseAttrValue> getAttrValueList(long attrId);
}
