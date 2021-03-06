package com.atguigu.gmall.service.annotation;

import com.atguigu.gmall.service.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/20 14:46
 */
@Import({GlobalExceptionHandler.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAppGlobalExceptionHander {
}
