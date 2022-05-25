package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.cron.SkuIdBloomTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 18:43
 */
@RestController
@RequestMapping("/admin/product")
public class CacheController {

    @Autowired
    private SkuIdBloomTask skuIdBloomTask;

    @RequestMapping("/rebuild")
    public Result rebuild(){
        skuIdBloomTask.rebuildBloom();
        return Result.ok();
    }
}
