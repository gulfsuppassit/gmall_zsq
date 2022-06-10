package com.atguigu.gmall.pay.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.pay.service.AliPayService;
import io.lettuce.core.LPosArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 16:38
 */
//http://api.gmall.com/api/payment/alipay/submit/741330085885247488
@RequestMapping("/api/payment/alipay")
@RestController
public class AliPayController {

    @Autowired
    AliPayService aliPayService;

    @GetMapping(value = "/submit/{orderId}",produces = "text/html;charset=utf-8")
    public String submit(@PathVariable("orderId") Long orderId) throws AlipayApiException {
        return aliPayService.submitAliPay(orderId);
    }

}
