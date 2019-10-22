package com.funsonli.springbootdemo030jdbctemplate.entity;

import com.funsonli.springbootdemo030jdbctemplate.util.SnowFlake;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Component
public class Student {
    private static final long serialVersionUID = 1L;
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private Integer age;
}
