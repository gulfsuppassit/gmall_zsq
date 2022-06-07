package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 9:07
 */
@Controller
public class OrderController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/trade.html")
    public String orderTradePage(Model model){
        Result<Map<String, Object>> result = orderFeignClient.getOrderConfirmVo();
        if (result.isOk()) {
            Map<String, Object> confirm = result.getData();
            model.addAllAttributes(confirm);
        }
        return "order/trade";
    }

    @GetMapping("/myOrder.html")
    public String myOrderPage(){

        return "order/myOrder";
    }
}
