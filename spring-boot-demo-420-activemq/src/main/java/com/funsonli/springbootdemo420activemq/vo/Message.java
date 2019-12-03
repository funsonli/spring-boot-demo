package com.funsonli.springbootdemo420activemq.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/15
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String message;
}
