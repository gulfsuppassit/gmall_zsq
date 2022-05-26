package com.atguigu.gmall.cache.config;

import com.atguigu.gmall.cache.constant.RedisConstant;
import com.atguigu.gmall.cache.service.BloomTask;
import com.atguigu.gmall.cache.service.SkuBloomTask;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 11:15
 */
@Slf4j
@AutoConfigureAfter(RedisAutoConfiguration.class) //必须在redis自动配置之后再进行
@Configuration
public class RedissonConfiguration {

        @Autowired(required = false)
        private List<BloomTask> bloomTasks;

        /**
         *
         * @param redisProperties  自动注入redis的配置
         * @return
         */
        @Bean
        public RedissonClient redissonClient(RedisProperties redisProperties){
                Config config = new Config();
                //Redis url should start with redis:// or rediss:// (for SSL connection)

                config.useSingleServer()
                        .setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort())
                        .setPassword(redisProperties.getPassword());


                RedissonClient redisson = Redisson.create(config);
                return  redisson;
        }

        @Bean
        public  RBloomFilter<Object> skuIdBloom(RedissonClient redissonClient){
                RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConstant.BLOOM_SKU_ID);
                if (filter.isExists()) {
                        log.info("redis已经装配好了sku布隆");
                        return filter;
                }else{
                        log.info("redis中还未装配sku布隆,正在进行初始化创建");
                        //redis中没有关于skuId的bloom数据
                        // 初始化布隆
                        filter.tryInit(5000000,0.000001);
                        for (BloomTask bloomTask : bloomTasks) {
                                log.info("sku布隆正在初始化数据....");
                                if (bloomTask instanceof SkuBloomTask) {
                                        bloomTask.initData(filter);
                                }
                        }
                }
                return filter;
        }


}
