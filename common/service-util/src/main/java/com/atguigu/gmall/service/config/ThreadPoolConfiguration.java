package com.atguigu.gmall.service.config;

import com.atguigu.gmall.service.constant.ThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 18:09
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {

    @Primary
    @Bean
    public ThreadPoolExecutor corePool(ThreadPoolProperties threadPoolProperties,@Value("spring.application.name")String name){
        /**
         * int corePoolSize,
         * int maximumPoolSize,
         * long keepAliveTime,
         * TimeUnit unit,
         * BlockingQueue<Runnable> workQueue,
         * ThreadFactory threadFactory,
         * RejectedExecutionHandler handler
         */
        log.info("核心线程准备完成");
       return new ThreadPoolExecutor(threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(),
                threadPoolProperties.getUnit(),
                new LinkedBlockingQueue(threadPoolProperties.getQueueNumber()),
                new appThreadFactory("["+name+"]-core-"),
                threadPoolProperties.getHandler());

    }

    @Bean
    public ThreadPoolExecutor otherPool(ThreadPoolProperties threadPoolProperties,@Value("spring.application.name")String name){
        /**
         * int corePoolSize,
         * int maximumPoolSize,
         * long keepAliveTime,
         * TimeUnit unit,
         * BlockingQueue<Runnable> workQueue,
         * ThreadFactory threadFactory,
         * RejectedExecutionHandler handler
         */
        log.info("非核心线程准备完成");
        return new ThreadPoolExecutor(threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize()/2,
                threadPoolProperties.getKeepAliveTime()/2,
                threadPoolProperties.getUnit(),
                new LinkedBlockingQueue(threadPoolProperties.getQueueNumber()/20),
                new appThreadFactory("["+name+"]-core-"),
                threadPoolProperties.getHandler());

    }

    class appThreadFactory implements ThreadFactory{

        private String appName;
        private AtomicInteger integer = new AtomicInteger(1);

        public appThreadFactory(String appName) {
            this.appName = appName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(this.appName+integer);
            return thread;
        }
    }
}
