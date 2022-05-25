package com.atguigu.gmall.item.config;

import com.atguigu.gmall.service.annotation.EnableRedissonAndCache;
import com.atguigu.gmall.service.annotation.EnableThreadPoolConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 13:50
 */
@EnableThreadPoolConfiguration
@EnableRedissonAndCache
@EnableScheduling
@Configuration
public class ItemConfig {
}
