package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailTo;
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
 * @date 2022/5/21 14:32
 */
@RestController
@RequestMapping("/rpc/inner/item")
public class DetailController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @GetMapping("/detail/{skuId}")
    public Result<ItemDetailTo> detail(@PathVariable("skuId")Long skuId){
        //获取分类信息
        ItemDetailTo itemDetailTo = new ItemDetailTo();
        Result<BaseCategoryView> baseCategoryViewResult = productFeignClient.getCategoryView(skuId);
        if (baseCategoryViewResult.isOk()){
            itemDetailTo.setCategoryView(baseCategoryViewResult.getData());
        }
        Result<SkuInfo> skuInfoResult = productFeignClient.getSkuInfo(skuId);
        if (skuInfoResult.isOk()){
            itemDetailTo.setSkuInfo(skuInfoResult.getData());
        }
        Result<BigDecimal> priceResult = productFeignClient.getPrice(skuId);
        if (priceResult.isOk()){
            itemDetailTo.setPrice(priceResult.getData());
        }
        Result<List<SpuSaleAttr>> listResult = productFeignClient.getSpuSaleAttrAndValueBySkuId(skuId);
        if (listResult.isOk()){
            itemDetailTo.setSpuSaleAttrList(listResult.getData());
        }
        Result<Map<String, String>> valuesSkuJsonResult = productFeignClient.getValuesSkuJson(skuId);
        if (valuesSkuJsonResult.isOk()) {
            Map<String, String> data = valuesSkuJsonResult.getData();
            itemDetailTo.setValuesSkuJson(JSONs.toStr(data));
        }
        return Result.ok(itemDetailTo);
    }

}
