package com.atguigu.gmall.feign.order;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 16:15
 */
@FeignClient("service-order")
@RequestMapping("/rpc/inner/order")
public interface OrderFeignClient {

    @GetMapping("/getOrderConfirmVo")
    Result<Map<String,Object>> getOrderConfirmVo();

}
