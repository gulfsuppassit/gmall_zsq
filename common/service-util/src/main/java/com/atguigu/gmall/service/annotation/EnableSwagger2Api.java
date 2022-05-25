package com.atguigu.gmall.service.annotation;

import com.atguigu.gmall.service.config.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/20 14:28
 */
@Import({Swagger2Config.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableSwagger2Api {
}
