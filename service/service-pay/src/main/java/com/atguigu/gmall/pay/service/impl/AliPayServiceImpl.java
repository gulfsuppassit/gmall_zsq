package com.atguigu.gmall.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.pay.config.AlipayConfig;
import com.atguigu.gmall.pay.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 17:30
 */
@Service
public class AliPayServiceImpl implements AliPayService {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public String submitAliPay(Long orderId) throws AlipayApiException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        JSONObject bizContent = biuldBizContent(orderInfo);
        alipayRequest.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
        if(response.isSuccess()){
            System.out.println("提交支付订单调用成功");
            return response.getBody();
        } else {
            System.out.println("提交支付订单调用失败");
            return "error";
        }
    }

    private JSONObject biuldBizContent(OrderInfo orderInfo) {
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderInfo.getOutTradeNo());
        bizContent.put("total_amount",  orderInfo.getTotalAmount());
        bizContent.put("subject", "尚品汇:"+ orderInfo.getTradeBody());
        bizContent.put("body", orderInfo.getTradeBody());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("time_expire", DateUtil.formatDate(orderInfo.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
        return bizContent;
    }

    @Override
    public String queryTrade(String outTradeNo) throws AlipayApiException {
        //获得初始化的AlipayClient

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        //bizContent.put("trade_no", "2014112611001004680073956707");
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        //请求
        return response.getBody();
    }

    @Override
    public boolean verifySign(Map<String,String> map) throws AlipayApiException {

        boolean b = AlipaySignature.rsaCheckV1(map, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
        return b; //调用SDK验证签名
    }
}
