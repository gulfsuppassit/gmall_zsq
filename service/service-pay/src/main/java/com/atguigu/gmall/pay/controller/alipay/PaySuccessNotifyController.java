package com.atguigu.gmall.pay.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.payment.PayNotifySuccessVo;
import com.atguigu.gmall.pay.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 21:04
 */
@Slf4j
@RequestMapping("/api/payment")
@RestController
public class PaySuccessNotifyController {

    @Autowired
    OrderFeignClient orderFeignClient;
    @Autowired
    AlipayConfig alipayConfig;

    @PostMapping("/notify/success")
    public String successNotify(PayNotifySuccessVo vo, @RequestParam Map<String,String> map) throws AlipayApiException {

//        System.out.println(map);
        boolean signVerified = AlipaySignature.rsaCheckV1(map, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType()); //调用SDK验证签名

        if (signVerified){
            //todo 验证成功,修改状态
            log.info("验证通过...");
            Result result = orderFeignClient.updateOrderStatusToPAID(vo.getOut_trade_no());
            if (result.isOk()) {
                return "success";
            }
            return "error";
        }

        log.info("验证失败...");
        return "no";
    }

}
