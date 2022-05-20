package com.atguigu.gmall.common.config;

import com.google.common.base.Predicates;
import io.swagger.models.Swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/19 16:17
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket productApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(productApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/admin/product/.*")))
                .build();
    }

    public ApiInfo productApiInfo(){
        return new ApiInfoBuilder()
                .title("产品接口-Api文档")
                .description("本文档是所有后台管理的api接口文档")
                .version("1.0")
                .contact(new Contact("atguigu", "http://atguigu.com", "2356582790@qq.com"))
                .build();
    }


}
