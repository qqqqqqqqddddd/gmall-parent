package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuImageMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-08-25 09:14:48
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;


    //spu大保存
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo info) {

        //基本信息存到spu_info
        spuInfoMapper.insert(info);
        Long spuId = info.getId();

        //图片存到spu_images
        List<SpuImage> imageList = info.getSpuImageList();
        for (SpuImage image : imageList) {
            image.setSpuId(spuId);

        }
        //批量保存图片
        spuImageService.saveBatch(imageList);

        //保存属性名和值
        List<SpuSaleAttr> spuSaleAttrList = info.getSpuSaleAttrList();
        for (SpuSaleAttr attr : spuSaleAttrList) {
            attr.setSpuId(spuId);

            //保存销售属性值的集合
            List<SpuSaleAttrValue> attrValueList = attr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : attrValueList) {
                value.setSpuId(spuId);
                String name = attr.getSaleAttrName();
                value.setSaleAttrName(name);
            }

            spuSaleAttrValueService.saveBatch(attrValueList);
        }
        spuSaleAttrService.saveBatch(spuSaleAttrList);


    }
}




