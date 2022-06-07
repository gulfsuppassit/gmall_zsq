package com.atguigu.gmall.service.annotation;

import com.atguigu.gmall.service.handler.GlobalExceptionHandler;
import com.atguigu.gmall.service.interceptor.UserIdRequestInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/6 19:54
 */
@Import({UserIdRequestInterceptor.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableFeignAutoHeaderInterceptor {
}
