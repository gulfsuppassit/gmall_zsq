package com.atguigu.gmall.cache.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 20:53
 */
public interface CacheService {
    <T> T getData(String key, TypeReference<T> typeReference);

    <T> void save(String key, T cacheData);
}
