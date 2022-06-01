package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/31 20:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AppAuthProperties {

    List<String> anyoneurls;

    List<String> denyurls;

    List<String> authurls;

    String loginPage;

}
