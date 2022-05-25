package com.atguigu.gmall.service.annotation;

import com.atguigu.gmall.service.config.MinioConfig;
import com.atguigu.gmall.service.config.RedissonConfiguration;
import com.atguigu.gmall.service.service.impl.CacheServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 13:52
 */
@Import({RedissonConfiguration.class, CacheServiceImpl.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableRedissonAndCache {
}
