package com.atguigu.gmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/17 20:28
 */
@SpringCloudApplication
// @SpringBootApplication
public class ServerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class,args);
    }
}
