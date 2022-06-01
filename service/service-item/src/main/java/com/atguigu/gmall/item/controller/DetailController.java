package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.DetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ItemDetailTo;
import com.atguigu.gmall.service.constant.RedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 14:32
 */
@RestController
@RequestMapping("/rpc/inner/item")
public class DetailController {
    @Autowired
    ProductFeignClient productFeignClient;
    @Autowired
    private DetailService detailService;

    @GetMapping("/detail/{skuId}")
    public Result<ItemDetailTo> detail(@PathVariable("skuId")Long skuId){
        //获取分类信息
        ItemDetailTo itemDetailTo = detailService.getDetail(skuId);
        BigDecimal data = productFeignClient.getPrice(skuId).getData();
        itemDetailTo.setPrice(data);
        //给redis中存一个zset,设置score,每次请求score加一,满一百,给es中skuInfo的分数加100
        detailService.incrHotScore(skuId);
        return Result.ok(itemDetailTo);
    }

}
