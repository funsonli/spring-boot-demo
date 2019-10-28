package com.funsonli.springbootdemo090blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.funsonli.springbootdemo090blog.util.SnowFlake;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Entity
@Table(name = "post")
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private String content;
    private Integer click = 0;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
