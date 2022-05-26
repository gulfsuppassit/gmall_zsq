package com.atguigu.gmall.cache.anno;

import com.atguigu.gmall.cache.aop.CacheAspect;
import com.atguigu.gmall.cache.util.CacheHelper;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/26 10:29
 */
@Import({CacheAspect.class, CacheHelper.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EnableRedissonAndCache
@Inherited
public @interface EnableAutoCache {
}
