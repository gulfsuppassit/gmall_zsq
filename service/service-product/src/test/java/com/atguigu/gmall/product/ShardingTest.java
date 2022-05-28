package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/27 16:20
 */
@SpringBootTest
public class ShardingTest {

    @Resource
    private BaseCategory1Mapper baseCategory1Mapper;

    @Test
    public void writeTest(){
        BaseCategory1 baseCategory1 = new BaseCategory1();
        baseCategory1.setName("哈哈");
        baseCategory1Mapper.insert(baseCategory1);
    }

    @Test
    public void readTest(){
        BaseCategory1 baseCategory1 = baseCategory1Mapper.selectById(18);
        System.out.println("第1次"+baseCategory1);
        BaseCategory1 baseCategory2 = baseCategory1Mapper.selectById(18);
        System.out.println("第2次"+baseCategory2);
        BaseCategory1 baseCategory3 = baseCategory1Mapper.selectById(18);
        System.out.println("第3次"+baseCategory3);
        BaseCategory1 baseCategory4 = baseCategory1Mapper.selectById(18);
        System.out.println("第4次"+baseCategory4);
    }

}
