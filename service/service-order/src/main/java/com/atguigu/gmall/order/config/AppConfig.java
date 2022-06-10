package com.atguigu.gmall.order.config;

import com.atguigu.gmall.service.annotation.EnableAppGlobalExceptionHander;
import com.atguigu.gmall.service.annotation.EnableFeignAutoHeaderInterceptor;
import com.atguigu.gmall.service.annotation.EnableThreadPoolConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/7 18:47
 */
@EnableTransactionManagement
@EnableRabbit
@EnableAppGlobalExceptionHander
@EnableThreadPoolConfiguration
@EnableFeignAutoHeaderInterceptor
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.cart",
        "com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.user",
        "com.atguigu.gmall.feign.ware",
        "com.atguigu.gmall.feign.pay"})
@MapperScan("com.atguigu.gmall.order.mapper")
@Configuration
public class AppConfig {
}
