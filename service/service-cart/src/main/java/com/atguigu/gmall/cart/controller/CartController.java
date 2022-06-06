package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 14:12
 */
//http://api.gmall.com/api/cart/cartList
@RequestMapping("/api/cart")
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @Decription : 获取购物车列表
     */
    @GetMapping("/cartList")
    public Result<List<CartInfo>> getCartList() {
        //获取购物车列表
        List<CartInfo> cartList = cartService.getCartList();
        return Result.ok(cartList);
    }

    //http://api.gmall.com/api/cart/addToCart/49/1

    /**
     * 添加购物车商品数量
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result addToCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("num")Integer num){
        cartService.addToCart(skuId,num);
        return Result.ok();
    }

    /**
     * 修改选中状态
     * @param skuId
     * @param isChecked
     * @return
     */
    //http://api.gmall.com/api/cart/checkCart/49/0
    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("isChecked")Integer isChecked){
        cartService.checkCart(skuId,isChecked);
        return Result.ok();
    }

    /**
     * 删除购物车项
     */
    // http://api.gmall.com/api/cart/deleteCart/44
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId")Long skuId){
        cartService.deleteCart(skuId);
        return Result.ok();
    }

    /**
     * 删除已选中的购物车
     * @return
     */
    //http://cart.gmall.com/cart/deleteChecked
    @GetMapping("/deleteChecked")
    public Result deleteChecked(){
        cartService.deleteChecked();
        return Result.ok();
    }

}
