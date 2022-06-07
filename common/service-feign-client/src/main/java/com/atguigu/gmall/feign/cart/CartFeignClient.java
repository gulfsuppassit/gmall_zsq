package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 19:28
 */
@RequestMapping("/rpc/inner/cart")
@FeignClient("service-cart")
public interface CartFeignClient {

    @GetMapping("/addCart/{skuId}")
    public Result<CartInfo> addCart(@PathVariable("skuId") Long skuId,
                                    @RequestParam("skuNum") Integer skuNum);

    @GetMapping("/deleteChecked")
    public Result deleteChecked();

    /**
     * 获取已经选中的购物车项
     * @return
     */
    @GetMapping("/getCheckedItems")
    public Result<List<CartInfo>> getCheckedItems();
}
