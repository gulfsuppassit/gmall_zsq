package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.LoginResponseVO;
import com.atguigu.gmall.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86180
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-05-31 18:19:57
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginResponseVO login(UserInfo userInfo);

    void logout(String token);
}
