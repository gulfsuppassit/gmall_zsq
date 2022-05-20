package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/19 10:01
 */
@RestController
@RequestMapping("/admin/product")
public class SpuController {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private BaseSaleAttrService baseSaleAttrService;
    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @GetMapping("/{page}/{limit}")
    public Result getSpuInfo(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit,
                             Long category3Id) {
        Page<SpuInfo> spuInfoPage = new Page<>(page, limit);
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id", category3Id);
        spuInfoService.page(spuInfoPage, queryWrapper);
        return Result.ok(spuInfoPage);
    }

    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList() {
        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);
    }


    /**
     * 添加spu商品信息,
     * 同时添加了spu_iamge商品图片信息
     * 和spu_sale_attr_value spu销售属性值
     *
     * @param spuInfo
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId") Long spuId) {
        QueryWrapper<SpuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(queryWrapper);
        return Result.ok(list);
    }


    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
}
