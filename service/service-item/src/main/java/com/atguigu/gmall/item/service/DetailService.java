package com.atguigu.gmall.item.service;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.ItemDetailTo;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/24 16:47
 */
public interface DetailService {

    public ItemDetailTo getDetailFromDB(Long skuId);

    public ItemDetailTo getDetail(Long skuId);

}
