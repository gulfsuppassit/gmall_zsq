package com.atguigu.gmall.model.order;

import com.atguigu.gmall.model.cart.CartInfoForOrderVo;
import com.atguigu.gmall.model.user.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 14:19
 */
@Data
public class OrderConfirmVo {

    private List<CartInfoForOrderVo> detailArrayList;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private List<UserAddress> userAddressList;
    private String tradeNo;//防重复提交


}
