package com.atguigu.gmall.feign.user;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 19:26
 */
@RequestMapping("/rpc/inner/user")
@FeignClient("service-user")
public interface UserFeignClient {

    @GetMapping("/userAddress")
    public Result<List<UserAddress>> getUserAddressList();

}
