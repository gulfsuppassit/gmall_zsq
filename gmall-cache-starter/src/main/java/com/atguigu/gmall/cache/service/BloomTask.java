package com.atguigu.gmall.cache.service;

import org.redisson.api.RBloomFilter;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 18:21
 */
public interface BloomTask {
    public void initData(RBloomFilter<Object> skuBloom);
}
