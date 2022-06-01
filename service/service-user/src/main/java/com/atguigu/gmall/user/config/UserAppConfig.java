package com.atguigu.gmall.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/31 18:16
 */

@MapperScan("com.atguigu.gmall.user.mapper")
@EnableTransactionManagement
@Configuration
public class UserAppConfig {


}
