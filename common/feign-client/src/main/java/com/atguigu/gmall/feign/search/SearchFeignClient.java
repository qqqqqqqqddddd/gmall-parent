package com.atguigu.gmall.feign.search;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {

    /**
     * 保存goods到es
     * @param goods
     * @return
     */
    @PostMapping("/goods")
    Result saveGoods(@RequestBody Goods goods);


    /**
     * 删除商品从es中
     * @param skuId
     * @return
     */
    @DeleteMapping("/goods/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);

    /**
     * 商品检索
     * @param paramVo
     * @return
     */
    @PostMapping("/goods/search")
    Result<SearchResponseVo>  search(@RequestBody SearchParamVo paramVo);


    /**
     * 商品的热度分服务
     * @param skuId
     * @return
     */
    @GetMapping("/goods/hotscore/{skuId}")
    Result increHotScore(@PathVariable("skuId") Long skuId,
                         @RequestParam("score") Long score);
}
