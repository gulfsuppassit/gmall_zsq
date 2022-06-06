package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
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
import java.util.List;

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
        //保存spuInfo基础信息
        baseMapper.insert(spuInfo);
        //保存spu图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuInfo.getId());
        }
//        spuImageList.forEach(item-> item.setSpuId(spuInfo.getId()));
        spuImageService.saveBatch(spuImageList);
        //保存spu销售属性和属性值信息
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(spuInfo.getId());
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                spuSaleAttrValue.setSpuId(spuInfo.getId());
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            }
            spuSaleAttrValueService.saveBatch(spuSaleAttrValueList);
        }
//        spuSaleAttrList.forEach(item->{
//            item.setSpuId(spuInfo.getId());
//            List<SpuSaleAttrValue> spuSaleAttrValueList = item.getSpuSaleAttrValueList();
//            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
//                spuSaleAttrValue.setSpuId(spuInfo.getId());
//                spuSaleAttrValue.setSaleAttrName(item.getSaleAttrName());
//            }
//            spuSaleAttrValueService.saveBatch(spuSaleAttrValueList);
//        });
        spuSaleAttrService.saveBatch(spuSaleAttrList);
    }
}




