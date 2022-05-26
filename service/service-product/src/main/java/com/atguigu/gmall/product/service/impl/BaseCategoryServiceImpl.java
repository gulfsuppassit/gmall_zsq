package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.cache.anno.Cache;
import com.atguigu.gmall.cache.service.CacheService;
import com.atguigu.gmall.service.constant.RedisConst;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.BaseCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseCategoryServiceImpl implements BaseCategoryService {

    @Resource
    private BaseCategory1Mapper baseCategory1Mapper;
    @Resource
    private BaseCategory2Mapper baseCategory2Mapper;
    @Resource
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private CacheService cacheService;


    @Cache("category1")
    @Override
    public List<BaseCategory1> getCategory1() {
        QueryWrapper<BaseCategory1> queryWrapper = new QueryWrapper<>();
        return baseCategory1Mapper.selectList(queryWrapper);
    }
    @Cache("category2byCategory1:#{#args[0]}")
    @Override
    public List<BaseCategory2> getCategory2ByC1Id(Long category1Id) {
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id", category1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }
    @Cache("category3byCategory2:#{#args[0]}")
    @Override
    public List<BaseCategory3> getCategory3ByC2Id(Long category2Id) {
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id", category2Id);
        return baseCategory3Mapper.selectList(queryWrapper);
    }

    @Cache(RedisConst.CATEGORY_KEY)
    @Override
    public List<CategoryAndChildTo> getCategoryAndChild() {
        return baseCategory1Mapper.getCategoryAndChild();
    }

    @Cache("categoryView")
    @Override
    public BaseCategoryView getCategoryView(Long skuId) {

       return baseCategory1Mapper.getCategoryView(skuId);

    }
}
