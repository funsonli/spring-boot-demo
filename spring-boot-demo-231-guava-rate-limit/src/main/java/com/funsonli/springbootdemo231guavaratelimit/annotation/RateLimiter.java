package com.funsonli.springbootdemo231guavaratelimit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 * @author funsonli
 */
//作用于方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    // 限制次数
    double limit() default 5;

    //超时时长
    int timeout() default 1000;

    //超时时间单位
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
