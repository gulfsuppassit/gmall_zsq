package com.atguigu.gmall.cache;

import com.atguigu.gmall.cache.anno.EnableAutoCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/26 11:38
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
@EnableAutoCache
public class CacheAutoConfiguration {


}
