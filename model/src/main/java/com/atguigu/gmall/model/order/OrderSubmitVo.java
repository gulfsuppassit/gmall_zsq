package com.atguigu.gmall.model.order;

import com.atguigu.gmall.model.cart.CartInfoForOrderVo;
import lombok.Data;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 15:01
 */
@Data
public class OrderSubmitVo {

    private String consignee;
    private String consigneeTel;
    private String deliveryAddress;
    private String paymentWay;
    private String orderComment;
    private List<CartInfoForOrderVo> orderDetailList;

}
