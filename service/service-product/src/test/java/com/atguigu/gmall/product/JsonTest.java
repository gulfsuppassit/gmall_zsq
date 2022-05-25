package com.atguigu.gmall.product;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 21:22
 */
@SpringBootTest
public class JsonTest {

    @Resource
    private BaseCategory1Mapper baseCategory1Mapper;

    @Test
    public void test() {
        List<CategoryAndChildTo> categoryAndChild = baseCategory1Mapper.getCategoryAndChild();
        String s = JSONs.toStr(categoryAndChild);
        List<CategoryAndChildTo> list = JSONs.strToObj(s, new TypeReference<List<CategoryAndChildTo>>() {
        });
        System.out.println("list = " + list);
    }

    @Test
    public void test2() {
        String s = null;
        List<CategoryAndChildTo> list = JSONs.strToObj(s, new TypeReference<List<CategoryAndChildTo>>() {
        });
        System.out.println("list = " + list);
    }
}
