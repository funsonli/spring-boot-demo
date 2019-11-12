package com.funsonli.springbootdemo320elasticsearch.entity;

import com.funsonli.springbootdemo320elasticsearch.util.SnowFlake;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Document(indexName = "name", type = "name", shards = 1, replicas = 0)
@Data
public class Student {
    private static final long serialVersionUID = 1L;
    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private Integer age;
}
