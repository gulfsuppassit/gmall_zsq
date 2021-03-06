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
        log.info("UserAuthFilter ??????????????????????????????{}",path);

        List<String> anyoneurls = appAuthProperties.getAnyoneurls();
        for (String anyoneurl : anyoneurls) {
            //1.???????????????????????????????????????
            boolean match = matcher.match(anyoneurl, path);
            if (match) {
                //????????????,????????????
                return chain.filter(exchange);
            }
        }

        //2.???????????????????????????????????????
        List<String> denyurls = appAuthProperties.getDenyurls();
        for (String denyurl : denyurls) {
            boolean match = matcher.match(denyurl, path);
            if (match){
                //????????????????????????????????????
                Result<String> result = Result.build("", ResultCodeEnum.FORBIDDEN);
                String json = JSONs.toStr(result);
                DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
                Publisher<? extends DataBuffer> body = Mono.just(wrap);
                //????????????????????????
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                return exchange.getResponse().writeWith(body);
            }
        }

        //???3?????????????????????????????????
        List<String> authurls = appAuthProperties.getAuthurls();
        for (String authurl : authurls) {
            boolean match = matcher.match(authurl, path);
            if (match){
                //??????token
                boolean verify = verifyToken(request);
                if (!verify){
                    //????????????,??????????????????
                    return locationToLoginPage(exchange);
                }
            }
        }

        //???4??????????????????
        //1??????????????????????????????
        String token = getToken(request);
        if (StringUtils.isEmpty(token)){
            //????????????,???????????????,????????????
            return chain.filter(exchange);
        }else{
            //????????????,???????????????,????????????,??????????????????
            boolean verifyToken = verifyToken(request);
            if (!verifyToken){
                //?????????,???????????????????????????
                return locationToLoginPage(exchange);
            }else{
                //?????????,??????userId
                ServerHttpRequest originRequest = exchange.getRequest();
                UserInfo userInfo = getTokenFromRedis(request, token);
                ServerHttpRequest newRequest = originRequest.mutate()
                        .header("userId", userInfo.getId().toString())
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
//http://passport.gmall.com/login.html?originUrl=http://order.gmall.com/myOrder.html
    private Mono<Void> locationToLoginPage(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        //???????????????
        //1.????????????????????????:302
        response.setStatusCode(HttpStatus.FOUND);
        //2.???????????????Location ??????????????????
        String originUrl = request.getURI().toString();
        URI uri = URI.create(appAuthProperties.getLoginPage() +"?originUrl="+ originUrl);
        response.getHeaders().setLocation(uri);
        //3.????????????

        //4.?????????????????????????????????????????????cookie????????????????????????????????????????????????cookie???
        ResponseCookie token = ResponseCookie
                .from("token", "ssss")
                .maxAge(0L)
                .domain(".gmall.com")
                .build();
        response.addCookie(token);
        //response.setComplete()????????????,??????Mono<Void>
        return response.setComplete();
    }

    /**
     * ??????token??????????????????????????????
     * @param request
     * @return
     */
    private boolean verifyToken(ServerHttpRequest request) {
        String token = getToken(request);
        if (StringUtils.isEmpty(token)){
            //????????????token?????????,??????????????????token
            return false;
        }else{
            //????????????token,???????????????token??????,?????????????????????token????????????
            UserInfo userInfo = getTokenFromRedis(request,token);

            if (userInfo == null){
                //?????????,????????????????????????,??????ip?????????????????????
                return false;
            }
            return true;
        }
    }

    private String getToken(ServerHttpRequest request) {
        String token = null;
        //??????token,?????????????????????????????????????????????token?????????cookie?????????token,?????????????????????????????????
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
