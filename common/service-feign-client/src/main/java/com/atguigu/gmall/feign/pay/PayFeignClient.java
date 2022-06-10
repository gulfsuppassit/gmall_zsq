package com.atguigu.gmall.feign.pay;

import com.atguigu.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 19:48
 */
@RequestMapping("/rpc/inner/payment")
@FeignClient("service-pay")
public interface PayFeignClient {

    @GetMapping("/query/{outTradeNo}")
    Result<String> queryTrade(@PathVariable("outTradeNo") String outTradeNo);

    @PostMapping("/verifySign")
    Result<Boolean> verifySign(@RequestBody Map<String,String> map);

//    @GetMapping("/query/{outTradeNo}")
//    public void verifyPaymentStatusAndUpdateStatus(@PathVariable("outTradeNo") String outTradeNo);
}
