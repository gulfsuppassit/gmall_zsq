package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 11:38
 */

@Controller
public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;

    @GetMapping("/cart.html")
    public String toCartPage(){
        return "cart/index";
    }

    //http://cart.gmall.com/addCart.html?skuId=43&skuNum=1&sourceType=query
    @GetMapping("/addCart.html")
    public String toAddCart(@RequestParam("skuId")Long skuId,
                            @RequestParam("skuNum")Integer skuNum,
                            Model model){
        //远程调用cart
        Result<CartInfo> cartInfoResult = cartFeignClient.addCart(skuId, skuNum);
        if (cartInfoResult.isOk()){
            model.addAttribute("skuInfo", cartInfoResult.getData());
            model.addAttribute("skuNum",cartInfoResult.getData().getSkuNum());
        }
        return "cart/addCart";
    }

    /**
     * 删除选中购物车商品
     */
    //http://cart.gmall.com/cart/deleteChecked
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){
        cartFeignClient.deleteChecked();
        return "cart/index";
    }

}
