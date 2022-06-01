package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 11:38
 */

@Controller
public class CartController {

    public String toCartPage(){

        return "cart/index";
    }

}
