package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.service.BaseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 10:23
 */
@RestController
@RequestMapping("/rpc/inner/product")
public class CategoryRpcController {

    @Autowired
    private BaseCategoryService baseCategoryService;

    @GetMapping("/categorys")
    public Result<List<CategoryAndChildTo>> getCategoryAndChild() {
       List<CategoryAndChildTo> list = baseCategoryService.getCategoryAndChild();
        return Result.ok(list);
    }

    @GetMapping("/getCategoryView/{skuId}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("skuId") Long skuId) {
        BaseCategoryView baseCategoryView = baseCategoryService.getCategoryView(skuId);
        return Result.ok(baseCategoryView);
    }

}
