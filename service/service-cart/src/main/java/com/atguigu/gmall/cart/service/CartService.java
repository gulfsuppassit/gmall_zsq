package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 18:15
 */
public interface CartService {

    /**
     * 更新购物车商品价格
     * @param cartKey
     * @param skuId
     * @param price
     */
    void updateCartInfoPrice(String cartKey, Long skuId, BigDecimal price);

    /**
     * 判断这个购物车是否溢出
     *
     * @param cartKey
     * @return
     */
    void validationCartOverFlow(String cartKey);

    /**
     * 设置过期时间
     *
     * @param cartKey
     */
    void setCartTimeOut(String cartKey);

    /**
     * 添加购物车
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    CartInfo addCart(Long skuId, Integer skuNum);

    /**
     * 获取购物车商品列表
     *
     * @return
     */
    List<CartInfo> getCartList();

    /**
     * 添加购物车中商品数量
     *
     * @param skuId
     * @param num
     */
    void addToCart(Long skuId, Integer num);

    /**
     * 修改选中状态
     *
     * @param skuId
     * @param isChecked
     */
    void checkCart(Long skuId, Integer isChecked);

    /**
     * 根据skuId删除指定购物车项
     *
     * @param skuId
     */
    void deleteCart(Long skuId);

    /**
     * 删除选中的购物车项
     */
    void deleteChecked();

    /**
     * 删除购物车
     *
     * @param cartKey
     */
    void deleteCart(String cartKey);

}
