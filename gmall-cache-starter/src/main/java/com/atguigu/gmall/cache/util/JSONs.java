package com.atguigu.gmall.cache.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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


    public static <T> T strToObj(String value ,TypeReference<T> typeReference){
        T obj = null;
        try {
            obj = objectMapper.readValue(value, typeReference);
        } catch (Exception e) {
            log.error("出错了:{}",e);
        }
        return obj;
    }

    public static <T> T nullInstance(TypeReference<T> typeReference) {
        String json = "[]";  //Person
        T t = null;

        try {
            t = objectMapper.readValue(json, typeReference);
            //泛型套泛型  List<Map<String,Hello>>
            //aaa
        } catch (JsonProcessingException e) {
            log.error("准备空示例异常：{}",e);
            try {
                t = objectMapper.readValue("{}",typeReference);
            } catch (JsonProcessingException ex) {
                log.error("你这不是json");
            }
        }
        return t;
    }
}
