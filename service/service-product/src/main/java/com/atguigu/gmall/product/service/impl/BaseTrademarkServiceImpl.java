package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 86180
* @description 针对表【base_trademark(品牌表)】的数据库操作Service实现
* @createDate 2022-05-18 16:38:50
*/
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark>
    implements BaseTrademarkService{

    @Resource
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public Page<BaseTrademark> getBaseTrademarkPage(Long page, Long limit) {
        return null;
    }
}




