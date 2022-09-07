package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    SearchFeignClient searchFeignClient;

    /**
     * 检索列表页跳转
      * @return
     */
    @GetMapping("/list.html")
    public  String  search(SearchParamVo paramVo, Model model){

       Result<SearchResponseVo> search= searchFeignClient.search(paramVo);
        SearchResponseVo data = search.getData();

        //展示result数据到页面
       model.addAttribute("searchParam",data.getSearchParam());
       model.addAttribute("trademarkParam",data.getTrademarkParam());
       model.addAttribute("propsParamList",data.getPropsParamList());
       model.addAttribute("trademarkList",data.getTrademarkList());
       model.addAttribute("attrsList",data.getAttrsList());
       model.addAttribute("orderMap",data.getOrderMap());
       model.addAttribute("goodsList",data.getGoodsList());

       model.addAttribute("pageNo",data.getPageNo());
       model.addAttribute("totalPages",data.getTotalPages());

       model.addAttribute("urlParam",data.getUrlParam());

        return  "list/index";
    }
}
