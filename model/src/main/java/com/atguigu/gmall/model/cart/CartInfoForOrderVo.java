package com.atguigu.gmall.model.cart;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 14:02
 */
@Data
public class CartInfoForOrderVo {

    private String imgUrl;
    private String skuName;
    private BigDecimal orderPrice;
    private Integer skuNum;
    private String stock;

}
