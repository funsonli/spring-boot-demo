package com.funsonli.springbootdemo130exception.exception;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author Funsonli
 * @date 2019/11/19
 */
@Data
public class BootanException extends RuntimeException {

    private String message;

    public BootanException(String message) {
        super(message);
        this.message = message;
    }

}
