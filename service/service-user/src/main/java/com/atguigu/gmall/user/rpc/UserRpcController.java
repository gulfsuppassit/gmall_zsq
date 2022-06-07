package com.atguigu.gmall.user.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 19:21
 */
@RequestMapping("/rpc/inner/user")
@RestController
public class UserRpcController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/userAddress")
    public Result<List<UserAddress>> getUserAddressList() {
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", AuthUtil.getUserAuth().getUserId());
        List<UserAddress> list = userAddressService.list(queryWrapper);
        return Result.ok(list);
    }
}
