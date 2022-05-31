package com.atguigu.gmall.feign.list;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 10:18
 */
@RequestMapping("/rpc/inner/es")
@FeignClient("service-list")
public interface ListFeignClient {
    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods);

    @PostMapping("/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId);

    @PostMapping("/goods/search")
    public Result<SearchResponseVo> searchGoods(@RequestBody SearchParam searchParam);

    @GetMapping("/goods/incrHotScore/{skuId}")
    public Result updateHotScore(@PathVariable("skuId") Long skuId,
                                 @RequestParam("hotScore") Long score);
}
