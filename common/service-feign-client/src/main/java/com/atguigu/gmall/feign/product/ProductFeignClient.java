package com.atguigu.gmall.feign.product;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 11:45
 */
@FeignClient("service-product")
@RequestMapping("/rpc/inner/product")
public interface ProductFeignClient {

    @GetMapping("/categorys")
    public Result<List<CategoryAndChildTo>> getCategoryAndChild();

    @GetMapping("/getCategoryView/{skuId}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("skuId") Long skuId);

    @GetMapping("/getSkuInfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/getPrice/{skuId}")
    public Result<BigDecimal> getPrice(@PathVariable("skuId")Long skuId);

    @GetMapping("/getSpuSaleAttrAndValueBySkuId/{skuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrAndValueBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("/getValuesSkuJson/{skuId}")
    public Result<Map<String,String>> getValuesSkuJson(@PathVariable("skuId") Long skuId);
}
