package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/19 11:34
 */
@Api(tags = "sku相关api接口")
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("保存sku信息")
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    @ApiOperation("分页查询sku信息列表")
    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        skuInfoService.page(skuInfoPage);
        return Result.ok(skuInfoPage);
    }

    @ApiOperation("上架商品")
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSaleOrCancelSale(skuId,1);
        return Result.ok();
    }

    @ApiOperation("下架商品")
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSaleOrCancelSale(skuId,0);
        return Result.ok();
    }
}
