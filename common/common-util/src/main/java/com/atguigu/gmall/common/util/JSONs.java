package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 16:29
 */
@Slf4j
public class JSONs {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toStr(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON转换String异常对象:{}",obj);
            e.printStackTrace();
        }
        return null;
    }
}
