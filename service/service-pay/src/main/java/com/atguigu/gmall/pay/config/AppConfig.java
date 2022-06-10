package com.atguigu.gmall.pay.config;

import com.atguigu.gmall.service.annotation.EnableFeignAutoHeaderInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 17:01
 */
@EnableFeignAutoHeaderInterceptor
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.order"})
@Configuration
public class AppConfig {
}
