package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 20:46
 */
//http://api.gmall.com/api/order/auth/submitOrder?tradeNo=fcsdgsdrgdfghfg
@RequestMapping("/api/order/auth")
@RestController
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo orderSubmitVo) {
        Long orderId = orderInfoService.submitOrder(tradeNo, orderSubmitVo);
        return Result.ok(orderId);
    }

}
