package com.funsonli.springbootdemo111logaop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志aop
 *
 * @author Funson
 * @date 2018-06-11
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BootanLog {

    String value() default "";

    int type() default 1;
}
