package com.atguigu.gmall.service.config;

import com.atguigu.gmall.service.properties.MinioProperties;
import com.atguigu.gmall.service.service.OssService;
import com.atguigu.gmall.service.service.impl.OssServiceImpl;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 19:22
 */
//@Import(MinioProperties.class)
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        String endpoint = minioProperties.getEndpoint();
        String accessKey = minioProperties.getAccessKey();
        String secertKey = minioProperties.getSecertKey();
        MinioClient minioClient = new MinioClient(endpoint, accessKey, secertKey);
        boolean isExist = minioClient.bucketExists("gmall");
        if(isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket("gmall");
        }
        return minioClient;
    }

    @Bean
    public OssService ossService(){
        return new OssServiceImpl();
    }

}
