package com.atguigu.gmall.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 19:22
 */

@ConfigurationProperties(
        prefix = "app.minio"
)
@Data
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secertKey;
    private String bucket;

}
