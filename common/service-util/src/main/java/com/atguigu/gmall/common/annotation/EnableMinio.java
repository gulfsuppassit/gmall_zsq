package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.MinioConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 20:31
 */
@Import({MinioConfig.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableMinio {
}
