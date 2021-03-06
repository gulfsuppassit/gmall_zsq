package com.atguigu.gmall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 9:30
 */
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign")
@SpringCloudApplication
public class WebAllApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class,args);
    }
}
