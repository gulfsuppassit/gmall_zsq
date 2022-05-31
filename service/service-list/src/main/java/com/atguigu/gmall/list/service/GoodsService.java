package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 10:07
 */
public interface GoodsService {
    /**
     * 上架,保存商品信息
     * @param goods
     */
    void saveGoods(Goods goods);

    /**
     * 下架,从es中删除商品信息
     * @param skuId
     */
    void deleteGoods(Long skuId);

    /**
     * 检索
     * @param searchParam
     * @return
     */
    SearchResponseVo searchGoods(SearchParam searchParam);

    void incrHotScore(Long skuId, Long score);
}
