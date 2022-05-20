package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/19 11:34
 */
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;

    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        skuInfoService.page(skuInfoPage);
        return Result.ok(skuInfoPage);
    }

    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSale(skuId);
        return Result.ok();
    }


    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }
}
