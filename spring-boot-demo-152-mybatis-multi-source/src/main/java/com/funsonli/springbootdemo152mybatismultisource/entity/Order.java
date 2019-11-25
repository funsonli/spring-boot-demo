package com.funsonli.springbootdemo152mybatismultisource.entity;

import com.funsonli.springbootdemo152mybatismultisource.util.SnowFlake;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String studentId;
    private String sn;
    private Integer amount;
}
