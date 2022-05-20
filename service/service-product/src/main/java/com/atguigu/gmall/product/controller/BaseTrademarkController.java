package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description: 操作品牌表
 * @date 2022/5/18 16:40
 */
@Api("品牌相关的api接口")
@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @ApiOperation("分页查询品牌列表")
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result getBaseTrademarkList(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
//        Page<BaseTrademark> trademarkPage = baseTrademarkService.getBaseTrademarkPage(page,limit);
        Page<BaseTrademark> trademarkPage = new Page<>(page, limit);
        baseTrademarkService.page(trademarkPage);
        return Result.ok(trademarkPage);
    }

    @ApiOperation("保存品牌信息")
    @Transactional
    @PostMapping("/baseTrademark/save")
    public Result save(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @ApiOperation("修改品牌信息")
    @Transactional
    @PutMapping("/baseTrademark/update")
    public Result update(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    @ApiOperation("删除品牌信息")
    @Transactional
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result remove(@PathVariable("id") Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    @GetMapping("/baseTrademark/get/{id}")
    public Result get(@PathVariable("id") Long id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    @ApiOperation("获取品牌信息列表")
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        return Result.ok(baseTrademarkService.list());
    }
}
