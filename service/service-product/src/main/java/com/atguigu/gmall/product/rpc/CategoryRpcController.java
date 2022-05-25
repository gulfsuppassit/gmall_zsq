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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String,Object> cache = new HashMap<>();

    @GetMapping("/categorys")
    public Result<List<CategoryAndChildTo>> getCategoryAndChild() {
        /**
         * 方法一:用本地内存
         */
        /*if (cache.get("categorys") == null){
            List<CategoryAndChildTo> list = baseCategoryService.getCategoryAndChild();
            cache.put("categorys",list);
        }
        List<CategoryAndChildTo> list = (List<CategoryAndChildTo>) cache.get("categorys");*/
        //方法二:整合redis
        List<CategoryAndChildTo> list = baseCategoryService.getCategoryAndChild();
        return Result.ok(list);
    }

    @GetMapping("/getCategoryView/{skuId}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("skuId") Long skuId) {
        BaseCategoryView baseCategoryView = baseCategoryService.getCategoryView(skuId);
        return Result.ok(baseCategoryView);
    }

}
