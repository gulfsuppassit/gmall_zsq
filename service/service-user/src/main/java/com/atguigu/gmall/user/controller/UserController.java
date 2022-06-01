package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.model.user.LoginResponseVO;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/31 18:33
 */
////http://api.gmall.com/api/user/passport/login
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        // 浏览器 -- nginx -- 网关 -- 微服务
        //设置Ip登录
        String ipAddress = IpUtil.getIpAddress(request);
        userInfo.setIpAddress(ipAddress);


        //用户登录
        LoginResponseVO responseVo = userInfoService.login(userInfo);
        if(responseVo == null){
            //没有登录成功
            return Result.build("", ResultCodeEnum.LOGIN_ERROR);
        }
        return Result.ok(responseVo);
    }

    //http://api.gmall.com/api/user/passport/logout
    @GetMapping("/passport/logout")
    public Result logout( @RequestHeader("token")String token){
        userInfoService.logout(token);
        return Result.ok();
    }

}
