package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 86180
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-05-19 11:03:10
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    /**
     * 获取spu销售属性列表
     * @param spuId spuInfo表的主键
     * @return spu销售属性列表
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrAndValueBySkuId(Long skuId);

    Map<String,String> getValuesSkuJson(Long skuId);
}
