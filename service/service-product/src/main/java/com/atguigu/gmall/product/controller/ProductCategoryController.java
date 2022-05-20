package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("分类相关的api接口")
@RestController
@RequestMapping("/admin/product")
public class ProductCategoryController {

    @Autowired
    private BaseCategoryService baseCategoryService;

    @ApiOperation("获取一级分类")
    @GetMapping("/getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> baseCategory1s = baseCategoryService.getCategory1();
        return Result.ok(baseCategory1s);
    }

    @ApiOperation("根据一级分类id获取二级分类")
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2ByC1Id(@PathVariable("category1Id") Long category1Id){
        List<BaseCategory2> baseCategory2s = baseCategoryService.getCategory2ByC1Id(category1Id);
        return Result.ok(baseCategory2s);
    }

    @ApiOperation("根据二级分类id获取三级分类")
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable("category2Id") Long category2Id){
        List<BaseCategory3> baseCategory3s = baseCategoryService.getCategory3ByC2Id(category2Id);
        return Result.ok(baseCategory3s);
    }
}
