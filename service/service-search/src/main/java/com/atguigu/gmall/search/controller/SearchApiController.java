package com.atguigu.gmall.search.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import com.atguigu.gmall.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inner/rpc/search")
public class SearchApiController {

    @Autowired
    GoodsService goodsService;

    /**
     * 保存商品信息到es
     * @param goods
     * @return
     */
    @PostMapping("/goods")
    public Result saveGoods(@RequestBody Goods goods) {

        goodsService.saveGoods(goods);
        return  Result.ok();
    }


    /**
     * 删除商品从es中
     * @param skuId
     * @return
     */
    @DeleteMapping("/goods/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId) {
        goodsService.deleteGoods(skuId);
        return  Result.ok();
    }

    /**
     * 商品检索
     * @param paramVo
     * @return
     */
    @PostMapping("/goods/search")
    public  Result<SearchResponseVo>  search(@RequestBody SearchParamVo paramVo){

       SearchResponseVo searchResponseVo =  goodsService.search(paramVo);
       return  Result.ok(searchResponseVo);
    }

    /**
     * 商品的热度更新
     * @param skuId
     * @param score
     * @return
     */
    @GetMapping("/goods/hotscore/{skuId}")
    public  Result increHotScore(@PathVariable("skuId") Long skuId,
                                 @RequestParam("score") Long score){

        goodsService.updateHotScore(skuId,score);
        return  Result.ok();
    }


}
