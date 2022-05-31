package com.atguigu.gmall.product.service.impl;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import java.util.Date;

import com.atguigu.gmall.feign.list.ListFeignClient;
import com.atguigu.gmall.model.list.Goods;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Autowired
    private ListFeignClient listFeignClient;
    @Resource
    private BaseTrademarkMapper baseTrademarkMapper;
    @Resource
    private BaseCategoryService baseCategoryService;

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
        if (status == 1){
            //上架,从es中加入商品信息
            Goods goods = this.getSkuInfoForSearch(skuId);

            listFeignClient.saveGoods(goods);
        }else{
            //下架,从es中删除商品信息
            listFeignClient.deleteGoods(skuId);
        }

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

    @Override
    public Goods getSkuInfoForSearch(Long skuId) {
        Goods goods = new Goods();

        SkuInfo skuInfo = baseMapper.selectById(skuId);
        //设置skuInfo相关信息
        goods.setId(skuInfo.getId());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        //封装品牌信息
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(skuInfo.getTmId());
        goods.setTmId(skuInfo.getTmId());
        goods.setTmName(baseTrademark.getTmName());
        goods.setTmLogoUrl(baseTrademark.getLogoUrl());
        //封装分类信息
        BaseCategoryView categoryView = baseCategoryService.getCategoryView(skuId);
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());
        //热度默认为0
        goods.setHotScore(0L);
        //设置属性信息
        List<SearchAttr> attrs = baseMapper.getBaseAttrNameAndValue(skuId);
        goods.setAttrs(attrs);
        return goods;
    }

}




