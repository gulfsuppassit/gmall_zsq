package com.atguigu.gmall.product.config;

import com.atguigu.gmall.cache.anno.EnableAutoCache;
import com.atguigu.gmall.service.annotation.EnableAppGlobalExceptionHander;
import com.atguigu.gmall.service.annotation.EnableMinio;
import com.atguigu.gmall.service.annotation.EnableSwagger2Api;
import com.atguigu.gmall.service.config.MybatisPlusConfig;
import com.atguigu.gmall.service.config.ThreadPoolConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 20:02
 */
@Configuration
//@EnableRedissonAndCache
//@EnableAutoCache
@EnableTransactionManagement
//@ComponentScan(basePackages = "com.atguigu.gmall")
@Import({MybatisPlusConfig.class, ThreadPoolConfiguration.class})
@EnableAppGlobalExceptionHander
@EnableMinio
@EnableFeignClients("com.atguigu.gmall.feign.list")
@EnableSwagger2Api
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
public class ProductConfig {
}


