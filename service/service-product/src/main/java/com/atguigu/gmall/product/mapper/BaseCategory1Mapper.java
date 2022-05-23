package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {

    List<CategoryAndChildTo> getCategoryAndChild();

    BaseCategoryView getCategoryView(@Param("skuId") Long skuId);
}
