package com.atguigu.gmall.web.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsq
 * @Description: 拦截器,在远程调用的新请求中加入请求头
 * @date 2022/6/1 18:17
 */
@Component
public class UserIdRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String userId = request.getHeader("UserId");
        if(userId != null){
            requestTemplate.header("UserId",userId);
        }


        String UserTempId = request.getHeader("UserTempId");
        if(UserTempId != null){
            requestTemplate.header("UserTempId",UserTempId);
        }

    }
}
