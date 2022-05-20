package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.annotation.EnableMinio;
import com.atguigu.gmall.common.config.MinioConfig;
import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.common.config.Swagger2Config;
import com.atguigu.gmall.common.service.impl.OssServiceImpl;
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
@EnableTransactionManagement
//@ComponentScan(basePackages = "com.atguigu.gmall")
@Import({MybatisPlusConfig.class, Swagger2Config.class})
@EnableMinio
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
public class ProductConfig {
}
