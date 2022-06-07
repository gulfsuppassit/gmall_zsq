package com.atguigu.gmall.cart.rpc;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 18:08
 */
@RestController
@RequestMapping("/rpc/inner/cart")
public class CartRpcController {

    @Autowired
    private CartService cartService;

    @GetMapping("/addCart/{skuId}")
    public Result<CartInfo> addCart(@PathVariable("skuId") Long skuId,
                                    @RequestParam("skuNum") Integer skuNum){
        CartInfo cartInfo = cartService.addCart(skuId,skuNum);


        return Result.ok(cartInfo);
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

    @GetMapping("/getCheckedItems")
    public Result<List<CartInfo>> getCheckedItems(){
        List<CartInfo> cartInfoList = cartService.getCheckedItems();
        return Result.ok(cartInfoList);
    }

}
