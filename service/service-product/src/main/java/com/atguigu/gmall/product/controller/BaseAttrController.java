package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Api("")
public class BaseAttrController {

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    @Autowired
    private BaseAttrValueService baseAttrValueService;

    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable("category1Id") Long category1Id,
                               @PathVariable("category2Id") Long category2Id,
                               @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoService.findAttrInfobyC1IdC2IdC3Id(category1Id, category2Id, category3Id);
        return Result.ok(infos);
    }


    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.saveOrUpdateAttrInfo(baseAttrInfo);
        return Result.ok();
    }


    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId) {
        List<BaseAttrValue> baseAttrValues = baseAttrValueService.selectByAttrId(attrId);
        return Result.ok(baseAttrValues);
    }
}
