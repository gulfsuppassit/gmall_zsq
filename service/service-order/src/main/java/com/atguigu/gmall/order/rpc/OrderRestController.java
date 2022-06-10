package com.atguigu.gmall.order.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.model.order.OrderConfirmVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderTradeVo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 15:17
 */
@RequestMapping("/rpc/inner/order")
@RestController
public class OrderRestController {

    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping("/getOrderConfirmVo")
    public Result<OrderConfirmVo> getOrderConfirmVo(){
        OrderConfirmVo list = orderInfoService.getOrderConfirmVo();
        return Result.ok(list);
    }

    @RequestMapping("/getOrderInfo/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable("orderId") Long orderId) {
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers
                .lambdaQuery(OrderInfo.class).eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, AuthUtil.getUserAuth().getUserId());
        OrderInfo orderInfo = orderInfoService.getOne(wrapper);
        return Result.ok(orderInfo);
    }

    @GetMapping("/updateOrderStatusToPAID/{outTradeNo}")
    public Result updateOrderStatusToPAID(@PathVariable("outTradeNo") String outTradeNo){
        orderInfoService.updateOrderStatusToPAID(outTradeNo);
        return Result.ok();
    }

    @GetMapping("/getPaymentStatus/{outTradeNo}")
    public Result<String> getPaymentStatus(@PathVariable("outTradeNo") String outTradeNo){
        String paymentStatus = orderInfoService.getPaymentStatus(outTradeNo);
        return Result.ok(paymentStatus);
    }

    @GetMapping("/checkOrderStatus/{outTradeNo}")
    public Result checkOrderStatus(@PathVariable("outTradeNo") String outTradeNo){
        orderInfoService.checkOrderStatusAndUpdateStatus(outTradeNo);
        return Result.ok();
    }

}
