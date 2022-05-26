package com.atguigu.gmall.item.config;

import com.atguigu.gmall.cache.anno.EnableAutoCache;
import com.atguigu.gmall.service.annotation.EnableThreadPoolConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 13:50
 */
@EnableThreadPoolConfiguration
//@EnableRedissonAndCache
@EnableScheduling
//@EnableAutoCache
@EnableAspectJAutoProxy
@Configuration
public class ItemConfig {
}
