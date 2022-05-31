package com.atguigu.gmall.list;

import com.atguigu.gmall.list.bean.Hello;
import com.atguigu.gmall.list.dao.HelloDao;
import com.atguigu.gmall.list.service.GoodsService;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.client.ElasticsearchClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/28 15:11
 */
@SpringBootTest
public class ESTest {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private HelloDao helloDao;
    @Autowired
    private GoodsService goodsService;

    @Test
    public void test01(){
        //restTemplate.
        Hello hello = new Hello(1L,"zsq",21);
        Hello hello1 = helloDao.save(hello);
        System.out.println(hello1);
    }

    @Test
    public void paramTest(){
        SearchParam param = new SearchParam();
        param.setCategory3Id(61L);
        param.setKeyword("PLUS");
        param.setTrademark("4:小米");
        String[] props = new String[]{"4:128GB:机身存储"};
        param.setProps(props);
        param.setOrder("2:asc");
        SearchResponseVo goods = goodsService.searchGoods(param);
        System.out.println(goods);
    }

}
/*
@Data
@Document(indexName = "hello")
class Hello{
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
}*/
