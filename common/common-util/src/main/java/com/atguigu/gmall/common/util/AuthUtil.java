package com.atguigu.gmall.common.util;

import com.atguigu.gmall.model.to.UserAuthTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/1 18:56
 */
public class AuthUtil {

    public static UserAuthTO getUserAuth(){
        UserAuthTO userAuthTO = new UserAuthTO();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userId = request.getHeader("UserId");
        String userTempId = request.getHeader("UserTempId");
        if(!StringUtils.isEmpty(userId)){
            userAuthTO.setUserId(Long.parseLong(userId));
        }

        if(!StringUtils.isEmpty(userTempId)){
            userAuthTO.setUserTempId(userTempId);
        }
        return userAuthTO;
    }

}
