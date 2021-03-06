package com.atguigu.gmall.product;

import com.google.common.hash.BloomFilter;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/25 16:38
 */
@SpringBootTest
public class BloomTest {

    @Autowired
    private Map<String, RBloomFilter<Object>> bloomFilterMap;

    @Test
    public void test(){
        System.out.println(bloomFilterMap);
    }

}
