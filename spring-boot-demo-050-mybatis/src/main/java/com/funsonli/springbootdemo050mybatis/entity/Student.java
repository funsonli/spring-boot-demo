package com.funsonli.springbootdemo050mybatis.entity;

import com.funsonli.springbootdemo050mybatis.util.SnowFlake;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.student")
public class Student {
    private static final long serialVersionUID = 1L;
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private Integer age;
}
