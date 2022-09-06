package com.atguigu.gmall.search.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "person" , shards = 1,replicas = 1)
@Data
public class Person {

    @Id
    private Long id;
    @Field(value = "first",type = FieldType.Keyword)  //keyword 不分词
    private String firstName;
    @Field(value = "last",type = FieldType.Keyword)
    private String lastName;
    @Field(value = "age")
    private  Integer age;
    @Field(value = "address")
    private  String address;


}
