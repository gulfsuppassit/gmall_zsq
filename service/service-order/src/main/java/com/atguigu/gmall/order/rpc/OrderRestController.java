package com.atguigu.gmall.order.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderConfirmVo;
import com.atguigu.gmall.model.order.OrderTradeVo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
