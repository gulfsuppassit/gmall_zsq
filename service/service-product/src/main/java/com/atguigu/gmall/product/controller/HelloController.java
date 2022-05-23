package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/20 14:49
 */
@RestController
@RequestMapping("/admin/product")
public class HelloController {

    @GetMapping("/hello/{num}")
    public Result hello(@PathVariable("num") Integer num) {
        if (num % 2 == 0){
            throw new GmallException(ResultCodeEnum.COUPON_GET);
        }
            return Result.ok();
    }

}
