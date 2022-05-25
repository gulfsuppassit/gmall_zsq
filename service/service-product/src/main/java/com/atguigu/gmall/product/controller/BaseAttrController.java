package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Api(tags = "基础属性相关接口")
public class BaseAttrController {

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    @Autowired
    private BaseAttrValueService baseAttrValueService;

    @ApiOperation("获取所有属性信息列表")
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable("category1Id") Long category1Id,
                               @PathVariable("category2Id") Long category2Id,
                               @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoService.findAttrInfobyC1IdC2IdC3Id(category1Id, category2Id, category3Id);
        return Result.ok(infos);
    }

    @ApiOperation("保存属性信息")
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@ApiParam("前台获取的需要保存的属性信息") @RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.saveOrUpdateAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    @ApiOperation("根据属性id获取属性值")
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId) {
        List<BaseAttrValue> baseAttrValues = baseAttrValueService.selectByAttrId(attrId);
        return Result.ok(baseAttrValues);
    }
}
