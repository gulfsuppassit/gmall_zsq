package com.atguigu.gmall.product;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.plaf.PanelUI;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 14:03
 */
@SpringBootTest
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test(){
        System.out.println(redissonClient);
    }

    @Test
    public void redissonBloomTest(){
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter("sku:Bloom");
        bloomFilter.tryInit(1000000,0.000001);
        bloomFilter.add(13);
        bloomFilter.add(12);
        bloomFilter.add(11);
        bloomFilter.add(15);
        System.out.println(bloomFilter.contains(18));
    }

}
