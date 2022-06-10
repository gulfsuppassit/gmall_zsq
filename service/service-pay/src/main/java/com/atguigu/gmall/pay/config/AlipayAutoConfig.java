package com.atguigu.gmall.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsq
 * @Description:
 * @date 2022/6/8 16:44
 */
@Configuration
//@EnableConfigurationProperties(AlipayConfig.class)
public class AlipayAutoConfig {

    @Autowired
    private AlipayConfig alipayConfig;

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getMerchantPrivateKey(),
                "json", alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(), alipayConfig.getSignType());
    }
}
