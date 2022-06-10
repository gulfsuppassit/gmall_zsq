package com.atguigu.gmall.pay.rpc.alipay;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.pay.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 19:50
 */
@RestController
@RequestMapping("/rpc/inner/payment")
public class AliPayRpcController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private AliPayService aliPayService;

    @GetMapping("/query/{outTradeNo}")
    public Result<String> queryTrade(@PathVariable("outTradeNo") String outTradeNo) throws AlipayApiException {
        String result = aliPayService.queryTrade(outTradeNo);
        return Result.ok(result);
    }

    /**
     * 验签
     * @return
     */
    @PostMapping("/verifySign")
    public Result<Boolean> verifySign(@RequestBody Map<String,String> map) throws AlipayApiException {
        boolean signVerify = aliPayService.verifySign(map);
        return Result.ok(signVerify);
    }

//    @GetMapping("/query/{outTradeNo}")
//    public void verifyPaymentStatusAndUpdateStatus(@PathVariable("outTradeNo") String outTradeNo) throws AlipayApiException {
//        String result = aliPayService.queryTrade(outTradeNo);
//        //查看数据库该订单的状态
//        String status = orderFeignClient.getPaymentStatus(outTradeNo).getData();
//        if (!result.equals(status)) {
//            orderFeignClient.updateOrderStatusToPAID(outTradeNo);
//        }
//    }

}
