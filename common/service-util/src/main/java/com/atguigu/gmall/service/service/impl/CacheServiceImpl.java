package com.atguigu.gmall.service.service.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.service.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 20:53
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public <T> T getData(String key, TypeReference<T> typeReference) {
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)){
            if ("no".equals(value)){
                T t = JSONs.nullInstance(typeReference);
                return t;
            }
            T t = JSONs.strToObj(value, typeReference);
            return t;
        }
      return null;
    }

    @Override
    public <T> void save(String key, T cacheData) {
        String value = JSONs.toStr(cacheData);
        if (cacheData == null){
            redisTemplate.opsForValue().set(key, "no",30, TimeUnit.MINUTES);
        }
        Double v = Math.random() * 1000000;
        Integer time = 1000 * 60 * 60 * 24 * 3 + v.intValue();
        redisTemplate.opsForValue().set(key, value,time, TimeUnit.MILLISECONDS);
    }
}
