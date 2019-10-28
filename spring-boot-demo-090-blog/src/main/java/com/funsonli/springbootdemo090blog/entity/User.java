package com.funsonli.springbootdemo090blog.entity;

import com.funsonli.springbootdemo090blog.util.SnowFlake;
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
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String username;
    private String password;
}
