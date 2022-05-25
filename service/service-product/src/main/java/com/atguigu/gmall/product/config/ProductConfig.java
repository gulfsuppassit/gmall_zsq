package com.atguigu.gmall.product.config;

import com.atguigu.gmall.service.annotation.EnableAppGlobalExceptionHander;
import com.atguigu.gmall.service.annotation.EnableMinio;
import com.atguigu.gmall.service.annotation.EnableRedissonAndCache;
import com.atguigu.gmall.service.annotation.EnableSwagger2Api;
import com.atguigu.gmall.service.config.MybatisPlusConfig;
import com.atguigu.gmall.service.config.ThreadPoolConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 20:02
 */
@Configuration
@EnableRedissonAndCache
@EnableTransactionManagement
//@ComponentScan(basePackages = "com.atguigu.gmall")
@Import({MybatisPlusConfig.class, ThreadPoolConfiguration.class})
@EnableAppGlobalExceptionHander
@EnableMinio
@EnableSwagger2Api
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
public class ProductConfig {
}


