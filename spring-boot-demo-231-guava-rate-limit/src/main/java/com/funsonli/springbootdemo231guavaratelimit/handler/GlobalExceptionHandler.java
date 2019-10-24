package com.funsonli.springbootdemo231guavaratelimit.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截
 *
 * @author Funsonli
 * @date 2019/10/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handler(RuntimeException e) {
        return e.getMessage();
    }
}
