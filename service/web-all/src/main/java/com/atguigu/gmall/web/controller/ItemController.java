package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.item.ItemFeignClient;
import com.atguigu.gmall.model.to.ItemDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 13:52
 */
@Controller
public class ItemController {

    @Autowired
    private ItemFeignClient itemFeignClient;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model) {
        Result<ItemDetailTo> detail = itemFeignClient.detail(skuId);
        ItemDetailTo itemDetail = detail.getData();
        model.addAttribute("categoryView", itemDetail.getCategoryView());
        model.addAttribute("skuInfo", itemDetail.getSkuInfo());
        model.addAttribute("price",itemDetail.getPrice());
        model.addAttribute("spuSaleAttrList",itemDetail.getSpuSaleAttrList());
        model.addAttribute("valuesSkuJson",itemDetail.getValuesSkuJson());
        return "item/index";
    }

}
