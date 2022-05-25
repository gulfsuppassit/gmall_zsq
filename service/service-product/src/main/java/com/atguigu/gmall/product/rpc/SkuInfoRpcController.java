package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/22 11:23
 */
@RestController
@RequestMapping("/rpc/inner/product")
public class SkuInfoRpcController {

    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @GetMapping("/getSkuInfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        QueryWrapper<SkuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id",skuId);
        List<SkuImage> skuImages = skuImageService.list(queryWrapper);
        skuInfo.setSkuImageList(skuImages);
        return Result.ok(skuInfo);
    }

    @GetMapping("/getPrice/{skuId}")
    public Result<BigDecimal> getPrice(@PathVariable("skuId")Long skuId){
        BigDecimal price = skuInfoService.getPrice(skuId);
        return Result.ok(price);
    }

    @GetMapping("/getSpuSaleAttrAndValueBySkuId/{skuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrAndValueBySkuId(@PathVariable("skuId") Long skuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.getSpuSaleAttrAndValueBySkuId(skuId);
        return Result.ok(spuSaleAttrList);
    }

    @GetMapping("/getValuesSkuJson/{skuId}")
    public Result<Map<String,String>> getValuesSkuJson(@PathVariable("skuId") Long skuId){
        Map<String,String> skuValueJson = spuSaleAttrService.getValuesSkuJson(skuId);
        return Result.ok(skuValueJson);
    }


}
