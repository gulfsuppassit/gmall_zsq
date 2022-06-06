package com.atguigu.gmall.service.handler;


import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("spring.application.name")
    private String serviceName;


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        Result fail = Result.fail();
        fail.setCode(500);
        fail.setMessage("服务内部异常");
        log.error("服务内部异常:{}",e.getMessage());
        return fail;
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result error(GmallException e){
        Result fail = Result.fail();
        fail.setCode(e.getCode());
        fail.setMessage(e.getMessage());
        log.error("全局异常:{}",e.getMessage());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        StringBuffer url = request.getRequestURL();
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("path",url);
        hashMap.put("params",map);
        hashMap.put("ServiceName",serviceName);
        fail.setData(hashMap);
        return fail;
    }
}
