package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 86180
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-05-19 11:36:40
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SkuImageService skuImageService;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        baseMapper.insert(skuInfo);
        //保存sku属性值信息
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
        //保存sku销售属性值信息
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
        //保存sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);
    }

    /**
     * 上下架
     *
     * @param skuId
     */
    @Override
    public void onSaleOrCancelSale(Long skuId,Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(status);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public BigDecimal getPrice(Long skuId) {
        return baseMapper.getPrice(skuId);
    }

    @Override
    public List<Long> getSkuInfoIds() {
        return baseMapper.getSkuInfoIds();
    }

}




