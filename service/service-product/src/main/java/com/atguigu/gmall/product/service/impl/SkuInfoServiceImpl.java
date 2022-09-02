package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
* @author niuzepeng
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-08-25 09:14:48
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //基本信息保存到sku_info
        save(skuInfo);
        Long skuId = skuInfo.getId();

        //图片保存到sku_image
        for (SkuImage skuImage : skuInfo.getSkuImageList()) {
            skuImage.setSkuId(skuId);

        }
        skuImageService.saveBatch(skuInfo.getSkuImageList());

        //平台属性保存到sku_attr_value
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(skuAttrValueList);


        //销售属性保存到sku_sale_attr_value
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSaleAttrValueId(skuId);
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
    }

    //sku下架
    @Override
    public void cancelSale(Long skuId) {
        //is_sale : 1-上架 ,0-下架
        skuInfoMapper.updateIsSale(skuId,0);
    }
    //sku上架
    @Override
    public void onSale(Long skuId) {
        skuInfoMapper.updateIsSale(skuId,1);
    }

    //api查询前台的商品的sku
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();

        //查询到skuInfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //sku的基本属性信息
        skuDetailTo.setSkuInfo(skuInfo);

        //sku的三级分类
        CategoryViewTo categoryViewTo= baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        skuDetailTo.setCategoryView(categoryViewTo);

        //查询商品图片
        List<SkuImage> imageList= skuImageService.getSkuImage(skuId);
        skuInfo.setSkuImageList(imageList);

         //实时价格查询
        BigDecimal price = get1010Price(skuId);
        skuDetailTo.setPrice(price);

        //查询商品的销售属性集合
        List<SpuSaleAttr> saleAttrsList = spuSaleAttrService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);
        skuDetailTo.setSpuSaleAttrList(saleAttrsList);

        //查询商品sku的所有兄弟的销售属性的组合关系
        Long spuId = skuInfo.getSpuId();
        String valuejson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        skuDetailTo.setValuesSkuJson(valuejson);



        return skuDetailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {
        BigDecimal price = skuInfoMapper.getRealPrice(skuId);
        return price;
    }

    /**
     * 优化 - 查询skuInfo
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getDetailSkuInfo(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return skuInfo;
    }
    /**
     * 优化 - 查询skuImages
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImage> getDetailSkuImages(Long skuId) {
        List<SkuImage> imageList= skuImageService.getSkuImage(skuId);
        return imageList;
    }

    /**
     * 优化 - 查询skuPrice
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getDetailSkuPrice(Long skuId) {
        BigDecimal price = get1010Price(skuId);
        return price;
    }

    @Override
    public List<Long> findAllSkuId() {

        // 100w 商品
        // 100w * 8byte = 800w 字节 = 8mb。
        //1亿数据，所有id从数据库传给微服务  800mb的数据量
        //分页查询。分批次查询。

        return skuInfoMapper.getAllSkuId();
    }
}




