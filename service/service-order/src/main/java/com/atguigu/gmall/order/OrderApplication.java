package com.atguigu.gmall.order;

import com.atguigu.gmall.service.annotation.EnableAppGlobalExceptionHander;
import com.atguigu.gmall.service.annotation.EnableFeignAutoHeaderInterceptor;
import com.atguigu.gmall.service.annotation.EnableThreadPoolConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/5 17:13
 */
@EnableAppGlobalExceptionHander
@EnableThreadPoolConfiguration
@EnableFeignAutoHeaderInterceptor
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.cart",
                                    "com.atguigu.gmall.feign.product",
                                    "com.atguigu.gmall.feign.user",
                                    "com.atguigu.gmall.feign.ware"})
@EnableTransactionManagement
@MapperScan("com.atguigu.gmall.order.mapper")
@SpringCloudApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
