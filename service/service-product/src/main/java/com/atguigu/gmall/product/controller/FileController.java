package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.service.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/18 18:56
 */
@Api(tags = "对象存储api接口")
@Slf4j
@RestController
@RequestMapping("/admin/product")
public class FileController {

    @Autowired
    private OssService ossService;

    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public Result upload(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        InputStream stream = file.getInputStream();
        String contentType = file.getContentType();
        long size = file.getSize();
        log.info("上传文件相关信息: 文件名:{"+filename+"},文件类型:{"+contentType+"},文件大小:{"+size+"},数据流:{"+stream+"}");
        String url = ossService.upload(file);
        return Result.ok(url);
    }


}
