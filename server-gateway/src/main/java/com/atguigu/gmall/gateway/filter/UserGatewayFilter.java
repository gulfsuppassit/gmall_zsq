package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.gateway.properties.AppAuthProperties;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.service.constant.RedisConst;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.IPAddress;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.spring.web.json.Json;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/31 20:08
 */
@Slf4j
@Component
public class UserGatewayFilter implements GlobalFilter {

    @Autowired
    private AppAuthProperties appAuthProperties;

    AntPathMatcher matcher = new AntPathMatcher();
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("UserAuthFilter 开始拦截；请求路径：{}",path);

        List<String> anyoneurls = appAuthProperties.getAnyoneurls();
        for (String anyoneurl : anyoneurls) {
            //1.匹配任何人都可以访问的路径
            boolean match = matcher.match(anyoneurl, path);
            if (match) {
                //匹配成功,直接放行
                return chain.filter(exchange);
            }
        }

        //2.拦截任何人都不能访问的路径
        List<String> denyurls = appAuthProperties.getDenyurls();
        for (String denyurl : denyurls) {
            boolean match = matcher.match(denyurl, path);
            if (match){
                //表示拒绝访问路径匹配成功
                Result<String> result = Result.build("", ResultCodeEnum.FORBIDDEN);
                String json = JSONs.toStr(result);
                DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
                Publisher<? extends DataBuffer> body = Mono.just(wrap);
                //设置字节编码格式
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                return exchange.getResponse().writeWith(body);
            }
        }

        //【3】、必须登录才能访问的
        List<String> authurls = appAuthProperties.getAuthurls();
        for (String authurl : authurls) {
            boolean match = matcher.match(authurl, path);
            if (match){
                //验证token
                boolean verify = verifyToken(request);
                if (!verify){
                    //验证失败,打回到登录页
                    return locationToLoginPage(exchange);
                }
            }
        }

        //【4】正常请求；
        //1、前端没带还是带错了
        String token = getToken(request);
        if (StringUtils.isEmpty(token)){
            //前端没带,没带就没带,直接放行
            //放行前,将userTempId也向下透传
            ServerHttpRequest oldRequest = exchange.getRequest();
//            HttpCookie userTempId = oldRequest.getCookies().getFirst("userTempId");
            String userTempId = getUserTempId(request);
            ServerHttpRequest newRequest = oldRequest.mutate()
                    .header("UserTempId", userTempId)
                    .build();
            ServerWebExchange newExchange = exchange.mutate()
                    .request(newRequest)
                    .response(exchange.getResponse())
                    .build();
            return chain.filter(newExchange);
        }else{
            //前端带了,但是带错了,要么不带,带了就要带对
            boolean verifyToken = verifyToken(request);
            if (!verifyToken){
                //带错了,因此直接跳转登录页
                return locationToLoginPage(exchange);
            }else{
                //带对了,透传userId
                ServerHttpRequest originRequest = exchange.getRequest();
                UserInfo userInfo = getTokenFromRedis(request, token);
                ServerHttpRequest newRequest = originRequest.mutate()
                        .header("UserId", userInfo.getId().toString())
                        .build();
                ServerWebExchange newExchange = exchange.mutate()
                        .request(newRequest)
                        .response(exchange.getResponse())
                        .build();
                return chain.filter(newExchange);
            }
        }
//        return chain.filter(exchange);
    }

    private String getUserTempId(ServerHttpRequest request) {
        String userTempId = "";

        HttpCookie cookie = request.getCookies().getFirst("userTempId");
        //有这个cookie，说明前端把token放到的cookie位置，给我们带来了
        if (cookie != null) {
            userTempId = cookie.getValue();
        } else {
            //前端没有放在cookie位置
            String headerValue = request.getHeaders().getFirst("userTempId");
            userTempId = headerValue;
        }

        return userTempId;
    }

//http://passport.gmall.com/login.html?originUrl=http://order.gmall.com/myOrder.html
    private Mono<Void> locationToLoginPage(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        //重定向原理
        //1.设置重定向状态码:302
        response.setStatusCode(HttpStatus.FOUND);
        //2.设置响应头Location 指定到新位置
        String originUrl = request.getURI().toString();
        URI uri = URI.create(appAuthProperties.getLoginPage() +"?originUrl="+ originUrl);
        response.getHeaders().setLocation(uri);
        //3.响应结束

        //4.命令浏览器删除之前的假令牌（删cookie，服务器只需要给浏览器发一个同名cookie）
        ResponseCookie token = ResponseCookie
                .from("token", "ssss")
                .maxAge(0L)
                .domain(".gmall.com")
                .build();
        response.addCookie(token);
        //response.setComplete()响应结束,返回Mono<Void>
        return response.setComplete();
    }

    /**
     * 验证token是否存在或者是否正确
     * @param request
     * @return
     */
    private boolean verifyToken(ServerHttpRequest request) {
        String token = getToken(request);
        if (StringUtils.isEmpty(token)){
            //如果这里token还为空,说明前端没带token
            return false;
        }else{
            //前端带了token,从缓存中拿token数据,判断前端携带的token是否正确
            UserInfo userInfo = getTokenFromRedis(request,token);

            if (userInfo == null){
                //未登录,或者带了个假令牌,或者ip地址不是原地址
                return false;
            }
            return true;
        }
    }

    private String getToken(ServerHttpRequest request) {
        String token = null;
        //获取token,因为不知道前端是在请求头中带的token还是在cookie中带的token,因此两边都需要确认一遍
        HttpCookie cookieToken = request.getCookies().getFirst("token");
        if (cookieToken != null){
            token = cookieToken.getValue();
        }else{
            token = request.getHeaders().getFirst("token");
        }
        return token;
    }

    private UserInfo getTokenFromRedis(ServerHttpRequest request, String token) {
        String ipAddress = IpUtil.getGatwayIpAddress(request);
        String userInfoStr = redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + token);

        if (StringUtils.isEmpty(userInfoStr)) {
            return null;
        } else {
            UserInfo userInfo = JSONs.strToObj(userInfoStr, new TypeReference<UserInfo>() {
            });
            String address = userInfo.getIpAddress();
            if (ipAddress.equals(address)) {
                return null;
            } else {
                return userInfo;
            }
        }
    }
}
