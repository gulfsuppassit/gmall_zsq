package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.LoginResponseVO;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.service.constant.RedisConst;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.user.service.UserInfoService;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author 86180
* @description 针对表【user_info(用户表)】的数据库操作Service实现
* @createDate 2022-05-31 18:19:57
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginResponseVO login(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", userInfo.getLoginName());
        queryWrapper.eq("passwd", MD5.encrypt(userInfo.getPasswd()));
        UserInfo selectOne = baseMapper.selectOne(queryWrapper);
        if (selectOne == null){
            //登录失败,账户密码错误
            return null;
        }
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        //获取访问者的ip地址
        //准备一个token
        String token = UUID.randomUUID().toString().replace("-", "");
        //去redis保存登陆成功的用户的认证信息
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + token, JSONs.toStr(selectOne), 7, TimeUnit.DAYS);
        loginResponseVO.setToken(token);
        loginResponseVO.setNickName(selectOne.getNickName());
        return loginResponseVO;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisConst.USER_LOGIN_KEY_PREFIX+token);
    }
}




