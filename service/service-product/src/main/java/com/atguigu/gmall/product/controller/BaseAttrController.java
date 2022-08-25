package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台平台属性管理
 */
@RestController
@RequestMapping("/admin/product")
public class BaseAttrController {

    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;


    /*
      * @description: 查询分类下的所有属性
      * @author: niuzp
      * @date: 2022/8/25 19:51
      **/
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
     public Result  getAttrInfoList(@PathVariable("c1Id") long c1Id,
                                    @PathVariable("c2Id") long c2Id,
                                    @PathVariable("c3Id") long c3Id){

       List<BaseAttrInfo> list= baseAttrInfoService.getAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);
          return Result.ok(list);
     }


    /*
     * @description:  保存属性信息,新增或者修改
     * @author: niuzp
     * @date: 2022/8/25 21:18
     **/
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo info) {

            baseAttrInfoService.saveAttrInfo(info);
            return  Result.ok();
    }


    /*
     * @description: 修改时回显,平台属性的信息
     * @author: niuzp
     * @date: 2022/8/25 21:46
     **/
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") long attrId) {
        List<BaseAttrValue> values = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(values);
    }
}
