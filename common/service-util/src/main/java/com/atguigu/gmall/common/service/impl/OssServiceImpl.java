package com.atguigu.gmall.common.service.impl;

import com.atguigu.gmall.common.config.MinioConfig;
import com.atguigu.gmall.common.properties.MinioProperties;
import com.atguigu.gmall.common.service.OssService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 19:33
 */
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws Exception {
        MinioClient minioClient = minioConfig.minioClient();
        String filename = UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
        InputStream stream = file.getInputStream();
        long size = file.getSize();
        String contentType = file.getContentType();
        String bucket = minioProperties.getBucket();
        PutObjectOptions objectOptions = new PutObjectOptions(size, -1);
        objectOptions.setContentType(contentType);
        minioClient.putObject(bucket, filename, stream, objectOptions);
        String url = minioProperties.getEndpoint()+minioProperties.getBucket()+"/"+filename;
        return url;
    }
}
