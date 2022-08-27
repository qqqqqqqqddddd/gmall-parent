package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author niuzepeng
* @description 针对表【sku_image(库存单元图片表)】的数据库操作Service
* @createDate 2022-08-25 09:14:48
*/
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> getSkuImage(Long skuId);

}
