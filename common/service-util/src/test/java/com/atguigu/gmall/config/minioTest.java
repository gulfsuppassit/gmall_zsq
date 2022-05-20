package com.atguigu.gmall.config;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 18:27
 */

public class minioTest {

    @Test
    public void test(){
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.220.130:9000/", "admin", "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("gmall");
            }

            // 使用putObject上传一个文件到存储桶中。
            /**
             * String bucketName,
             * String objectName,
             * InputStream stream,
             * PutObjectOptions options
             */
            FileInputStream stream = new FileInputStream("D:\\mewgulf\\gulf\\Snipaste_2021-12-28_15-15-57.jpg");
            PutObjectOptions objectOptions = new PutObjectOptions(stream.available(),-1);
            minioClient.putObject("gmall", "1.jpg", stream, objectOptions);
            System.out.println("上传结束");
        } catch(Exception e) {
            System.err.println("Error occurred: " + e);
        }
    }



}
