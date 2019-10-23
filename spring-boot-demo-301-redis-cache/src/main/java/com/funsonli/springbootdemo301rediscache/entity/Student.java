package com.funsonli.springbootdemo301rediscache.entity;

import com.funsonli.springbootdemo301rediscache.util.SnowFlake;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Entity
@Table(name = "student")
public class Student {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    private String name;

    private Integer age;
}
