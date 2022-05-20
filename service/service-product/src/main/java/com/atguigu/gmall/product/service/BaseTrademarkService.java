package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86180
* @description 针对表【base_trademark(品牌表)】的数据库操作Service
* @createDate 2022-05-18 16:38:50
*/
public interface BaseTrademarkService extends IService<BaseTrademark> {

    Page<BaseTrademark> getBaseTrademarkPage(Long page, Long limit);
}
