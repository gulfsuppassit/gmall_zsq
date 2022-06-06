package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 21:09
 */
//@SpringBootTest
public class RedisTest {

//    @Autowired
    private RedisTemplate redisTemplate;

//    @Test
    public void test1(){
        redisTemplate.opsForValue().set("hello", "value");
        Object hello = redisTemplate.opsForValue().get("hello");
        System.out.println(hello);

    }
}
