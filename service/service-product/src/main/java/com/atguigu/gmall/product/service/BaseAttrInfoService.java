package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-25 09:14:48
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(long c1Id, long c2Id, long c3Id);

    void saveAttrInfo(BaseAttrInfo info);

}
