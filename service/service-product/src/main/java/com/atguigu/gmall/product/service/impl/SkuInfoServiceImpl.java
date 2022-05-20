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
        for (SkuAttrValue skuAttrValue : skuInfo.getSkuAttrValueList()) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueService.save(skuAttrValue);
        }
        for (SkuSaleAttrValue skuSaleAttrValue : skuInfo.getSkuSaleAttrValueList()) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
            skuSaleAttrValueService.save(skuSaleAttrValue);
        }
        for (SkuImage skuImage : skuInfo.getSkuImageList()) {
            skuImage.setSkuId(skuInfo.getId());
            skuImageService.save(skuImage);
        }
    }

    /**
     * 上架
     *
     * @param skuId
     */
    @Override
    public void onSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        baseMapper.updateById(skuInfo);
    }

    /**
     * 下架
     * @param skuId
     */
    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        baseMapper.updateById(skuInfo);
    }
}




