package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    List<BaseAttrValue> selectByAttrId(Long attrId);
}
