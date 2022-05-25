package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 86180
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-05-19 11:36:40
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    BigDecimal getPrice(@Param("skuId") Long skuId);

    List<Long> getSkuInfoIds();

}




