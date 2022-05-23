package com.atguigu.gmall.feign.item;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.ItemDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/22 12:28
 */
@RequestMapping("/rpc/inner/item")
@FeignClient("service-item")
public interface ItemFeignClient {

    @GetMapping("/detail/{skuId}")
    Result<ItemDetailTo> detail(@PathVariable("skuId")Long skuId);

}
