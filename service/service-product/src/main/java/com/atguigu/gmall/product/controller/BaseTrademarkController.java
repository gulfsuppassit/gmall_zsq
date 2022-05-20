package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description: 操作品牌表
 * @date 2022/5/18 16:40
 */
@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result getBaseTrademarkList(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
//        Page<BaseTrademark> trademarkPage = baseTrademarkService.getBaseTrademarkPage(page,limit);
        Page<BaseTrademark> trademarkPage = new Page<>(page, limit);
        baseTrademarkService.page(trademarkPage);
        return Result.ok(trademarkPage);
    }

    @Transactional
    @PostMapping("/baseTrademark/save")
    public Result save(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @Transactional
    @PutMapping("/baseTrademark/update")
    public Result update(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

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

    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        return Result.ok(baseTrademarkService.list());
    }
}
