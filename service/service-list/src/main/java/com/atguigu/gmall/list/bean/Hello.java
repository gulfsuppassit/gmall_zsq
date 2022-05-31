package com.atguigu.gmall.list.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 9:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "hello")
public class Hello{
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
}
