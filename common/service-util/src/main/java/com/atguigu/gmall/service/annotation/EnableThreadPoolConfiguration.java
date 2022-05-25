package com.atguigu.gmall.service.annotation;

import com.atguigu.gmall.service.config.ThreadPoolConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 20:13
 */
@Import({ThreadPoolConfiguration.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableThreadPoolConfiguration {
}
