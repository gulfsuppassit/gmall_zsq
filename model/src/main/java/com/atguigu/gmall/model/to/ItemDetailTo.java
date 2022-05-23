package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/21 16:18
 */
@Data
@ApiModel(description = "商品详情页所需信息")
public class ItemDetailTo {
    @ApiModelProperty("分类信息")
    private BaseCategoryView categoryView;
    @ApiModelProperty("sku信息")
    private SkuInfo skuInfo;
    @ApiModelProperty("商品价格")
    private BigDecimal price;
    @ApiModelProperty("spu销售属性列表")
    private List<SpuSaleAttr> spuSaleAttrList;
    @ApiModelProperty("返回的sku值:119:120")
    private String valuesSkuJson;

}
