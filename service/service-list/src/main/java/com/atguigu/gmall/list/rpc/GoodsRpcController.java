package com.atguigu.gmall.list.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.GoodsService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 10:02
 */
@RequestMapping("/rpc/inner/es")
@RestController
public class GoodsRpcController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods){
        goodsService.saveGoods(goods);
        return Result.ok();
    }
    @PostMapping("/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId){
        goodsService.deleteGoods(skuId);
        return Result.ok();
    }

    @PostMapping("/goods/search")
    public Result<SearchResponseVo> searchGoods(@RequestBody SearchParam searchParam){
        SearchResponseVo result = goodsService.searchGoods(searchParam);

        return Result.ok(result);
    }

    @GetMapping("/goods/incrHotScore/{skuId}")
    public Result updateHotScore(@PathVariable("skuId") Long skuId,
                                 @RequestParam("hotScore") Long score){
        goodsService.incrHotScore(skuId,score);
        return Result.ok();
    }

}
