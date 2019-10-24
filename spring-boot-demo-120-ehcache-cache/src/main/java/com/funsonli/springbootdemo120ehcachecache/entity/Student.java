package com.funsonli.springbootdemo120ehcachecache.entity;

import com.funsonli.springbootdemo120ehcachecache.util.SnowFlake;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Entity
@Table(name = "student")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    private String name;

    private Integer age;
}
