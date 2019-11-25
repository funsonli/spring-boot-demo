package com.funsonli.springbootdemo152mybatismultisource.entity;

import com.funsonli.springbootdemo152mybatismultisource.util.SnowFlake;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Component
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private Integer age;
}
