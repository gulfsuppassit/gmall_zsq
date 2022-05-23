package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author 86180
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-05-19 11:36:40
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);

    void onSaleOrCancelSale(Long skuId,Integer status);

    BigDecimal getPrice(Long skuId);
}
