package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author niuzepeng
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-08-25 09:14:48
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    //查询指定分类下的所有属性
    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(long c1Id, long c2Id, long c3Id) {
        //sql

        List<BaseAttrInfo> infos= baseAttrInfoMapper.getAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);
        return infos;
    }

     //保存平台属性
     @Override
     public void saveAttrInfo(BaseAttrInfo info) {
         //修改或者新增判断
         if (info.getId() == null) {
             //进行新增操作
             addBaseAttrInfo(info);
         } else {
             updateBaseAttrInfo(info);

         }
     }

    private void updateBaseAttrInfo(BaseAttrInfo info) {
        //进行修改操作
        baseAttrInfoMapper.updateById(info);
        //修改属性值
        List<BaseAttrValue> valueList = info.getAttrValueList();

        //先删除
        List<Long> vids = new ArrayList<>();
        for (BaseAttrValue value : valueList) {
            Long id = value.getId();
            if (id !=null){
                vids.add(id);
            }
        }
        if (vids.size()>0){
            //部分删除
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            deleteWrapper.notIn("id",vids);
            baseAttrValueMapper.delete(deleteWrapper);
        }else {
            //全删除
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            baseAttrValueMapper.delete(deleteWrapper);
        }

        for (BaseAttrValue value : valueList) {
            if (value.getId() != null){
                baseAttrValueMapper.updateById(value);
            }else if (value.getId() == null){
                value.setAttrId(info.getId());
                baseAttrValueMapper.insert(value);
            }
        }
    }

    private void addBaseAttrInfo(BaseAttrInfo info) {
        //保存属性名
        baseAttrInfoMapper.insert(info);
        //获取自增的id
        Long id = info.getId();
        //保存属性值
        List<BaseAttrValue> valueList = info.getAttrValueList();

        for (BaseAttrValue value : valueList) {
            value.setAttrId(id);
            baseAttrValueMapper.insert(value);
        }
    }


}




