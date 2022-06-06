package com.atguigu.gmall.cart;

import com.atguigu.gmall.service.annotation.EnableThreadPoolConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 11:22
 */
@EnableThreadPoolConfiguration
@EnableFeignClients("com.atguigu.gmall.feign.product")
@SpringCloudApplication
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

}
