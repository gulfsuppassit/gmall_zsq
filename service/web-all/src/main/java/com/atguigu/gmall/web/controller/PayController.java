package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.feign.pay.PayFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.payment.PayNotifySuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 21:02
 */
@Controller
public class PayController {
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private PayFeignClient payFeignClient;


    //http://payment.gmall.com/pay.html?orderId=741037780301250560
    @GetMapping("/pay.html")
    public String toPaymentPage(@RequestParam("orderId")Long orderId, Model model) {
        Result<OrderInfo> orderInfo = orderFeignClient.getOrderInfo(orderId);
//        Long id = orderInfo.getData().getId();
        if (orderInfo.isOk()) {
            model.addAttribute("orderInfo",orderInfo.getData());
        }

        return "payment/pay.html";
    }

    @GetMapping("/payment/success")
    public String paySuccess(PayNotifySuccessVo vo, @RequestParam Map<String,String> map){
//        System.out.println(JSONs.toStr(map));
//        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        Boolean data = payFeignClient.verifySign(map).getData();
        if (data){
            //验签成功,验证本地与支付宝支付状态是否一致,如果不一致修改数据库状态
//            payFeignClient.verifyPaymentStatusAndUpdateStatus(vo.getOut_trade_no());
            orderFeignClient.checkOrderStatus(vo.getOut_trade_no());
        }
        return "/payment/success";
    }


}
