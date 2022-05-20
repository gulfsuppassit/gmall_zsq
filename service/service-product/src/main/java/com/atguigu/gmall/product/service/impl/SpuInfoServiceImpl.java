package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 86180
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-05-19 10:00:03
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        baseMapper.insert(spuInfo);
        spuInfo.getSpuImageList().forEach(item-> {
            item.setSpuId(spuInfo.getId());
            spuImageService.save(item);
        });
        spuInfo.getSpuSaleAttrList().forEach(item->{
            spuSaleAttrService.save(item);
            for (SpuSaleAttrValue spuSaleAttrValue : item.getSpuSaleAttrValueList()) {
                spuSaleAttrValue.setSpuId(spuInfo.getId());
                spuSaleAttrValue.setSaleAttrName(item.getSaleAttrName());
                spuSaleAttrValueService.save(spuSaleAttrValue);
            }
        });



    }
}




