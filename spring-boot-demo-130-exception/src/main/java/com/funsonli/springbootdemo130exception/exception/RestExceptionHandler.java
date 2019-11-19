package com.funsonli.springbootdemo130exception.exception;

import com.funsonli.springbootdemo130exception.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理类
 *
 * @author Funsonli
 * @date 2019/11/19
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BootanException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResult handleBootanException(BootanException e) {

        if (e != null) {
            log.info(e.toString(), e);
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.error();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResult handleException(BootanException e) {

        if (e != null) {
            log.info(e.toString(), e);
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.error();
    }

}
