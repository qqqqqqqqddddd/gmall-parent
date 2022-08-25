package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台品牌管理
 */

@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;


    /*
     * @description: 分页查询品牌
     * @author: niuzp
     * @date: 2022/8/25 23:25
     **/
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result baseTrademark(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Integer pageSize) {
        //分页查询,加入分页插件
        Page<BaseTrademark> page =new Page(pageNum,pageSize);
        Page<BaseTrademark> pageResult = baseTrademarkService.page(page);
        return Result.ok(pageResult);
    }

    /*
     * @description: 根据id查询品牌,回显
     * @author: niuzp
     * @date: 2022/8/25 23:25
     **/
    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademark(@PathVariable("id") Long id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /*
     * @description: 修改保存品牌信息
     * @author: niuzp
     * @date: 2022/8/25 23:25
     **/
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }


    /*
     * @description: 新增品牌信息
     * @author: niuzp
     * @date: 2022/8/25 23:25
     **/
    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }


    /*
     * @description: 删除一条品牌信息
     * @author: niuzp
     * @date: 2022/8/25 23:25
     **/
    @DeleteMapping("/baseTrademark/remove/{tid}")
    public Result deleteBaseTrademark(@PathVariable("tid") Long tid) {
        baseTrademarkService.removeById(tid);
        return Result.ok();
    }
}
