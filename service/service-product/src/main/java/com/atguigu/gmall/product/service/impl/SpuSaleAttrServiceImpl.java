package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86180
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-05-19 11:03:10
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        QueryWrapper<SpuSaleAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",spuId);
        List<SpuSaleAttr> spuSaleAttrList = baseMapper.selectList(queryWrapper);
        spuSaleAttrList.forEach(item->{
            QueryWrapper<SpuSaleAttrValue> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("spu_id",spuId);
            queryWrapper1.eq("sale_attr_name",item.getSaleAttrName());
            List<SpuSaleAttrValue> list = spuSaleAttrValueService.list(queryWrapper1);
            item.setSpuSaleAttrValueList(list);
        });
        return spuSaleAttrList;
    }
}




