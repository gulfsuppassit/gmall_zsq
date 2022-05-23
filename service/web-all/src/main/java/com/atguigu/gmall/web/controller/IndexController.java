package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 9:47
 */
@Controller
public class IndexController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @GetMapping("/")
    public String toIndex(Model model) {
        List<CategoryAndChildTo> list = productFeignClient.getCategoryAndChild().getData();
        model.addAttribute("list",list);
        return "index/index";
    }


}
