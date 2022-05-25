package com.atguigu.gmall.service.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 19:32
 */
public interface OssService {
    String upload(MultipartFile file) throws Exception;
}
