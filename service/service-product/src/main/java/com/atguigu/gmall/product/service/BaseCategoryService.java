package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;

import java.util.List;

public interface BaseCategoryService {
    List<BaseCategory1> getCategory1();

    List<BaseCategory2> getCategory2ByC1Id(Long category1Id);

    List<BaseCategory3> getCategory3ByC2Id(Long category2Id);

}
