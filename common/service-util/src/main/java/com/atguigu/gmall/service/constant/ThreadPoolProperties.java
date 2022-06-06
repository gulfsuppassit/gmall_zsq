package com.atguigu.gmall.service.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zsq
 * @Description: 线程池的常量配置
 * @date 2022/5/23 18:08
 */
//@Component
@Data
@ConfigurationProperties(prefix = "app.threadpool")
public class ThreadPoolProperties {
    private int corePoolSize = 8;
    private int maximumPoolSize = 16;
    private long keepAliveTime = 1;
    private TimeUnit unit=TimeUnit.MINUTES;
    private int queueNumber = 1000;
    private RejectedExecutionHandler handler=new ThreadPoolExecutor.CallerRunsPolicy();
}
