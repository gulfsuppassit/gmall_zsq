package com.atguigu.gmall.cache.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/25 19:31
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {

    @AliasFor("cacheKey")
    String value() default "";

    @AliasFor("value")
    String cacheKey() default "";

    String bloomName() default "";

    String bloomValue() default "";

}
