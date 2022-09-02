package com.atguigu.gmall.web.controller;



import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.web.feign.SkuFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ItemController {

    @Autowired
    SkuFeignClient skuFeignClient;

    /**
     * 商品的详情页展示
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String  item(@PathVariable("skuId") Long skuId,
                        Model model){

//      SkuDetailTo skuDetailTo = skuFeignClient.getSkuDetail(skuId);
        Result<SkuDetailTo> result = skuFeignClient.getSkuDetail(skuId);

        if(result.isOk()) {
            SkuDetailTo skuDetailTo = result.getData();

            if (skuDetailTo == null || skuDetailTo.getSkuInfo() == null) {
                //说明远程没有查到商品
                return "item/404";
            }

        //获取商品情:sku的全部属性

            model.addAttribute("categoryView",skuDetailTo.getCategoryView());
            model.addAttribute("skuInfo",skuDetailTo.getSkuInfo());
            model.addAttribute("price",skuDetailTo.getPrice());
            model.addAttribute("spuSaleAttrList",skuDetailTo.getSpuSaleAttrList());
            model.addAttribute("valuesSkuJson",skuDetailTo.getValuesSkuJson());

        }
        return  "item/index";
    }

}
