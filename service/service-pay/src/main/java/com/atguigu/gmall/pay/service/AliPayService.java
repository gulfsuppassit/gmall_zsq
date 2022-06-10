package com.atguigu.gmall.pay.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 17:30
 */
public interface AliPayService {
    /**
     * 跳到阿里支付页
     * @param orderId
     * @return
     */
    String submitAliPay(Long orderId) throws AlipayApiException;

    String queryTrade(String outTradeNo) throws AlipayApiException;

    public boolean verifySign(Map<String,String> map) throws AlipayApiException;
}
