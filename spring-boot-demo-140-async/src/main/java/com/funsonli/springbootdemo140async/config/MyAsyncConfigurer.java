package com.funsonli.springbootdemo140async.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class for
 * 
 * @author Funsonli
 * @date 2019/11/7
 */
@Slf4j
@Component
public class MyAsyncConfigurer implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        //定义一个核心为10个线程数量的线程池
        return new ThreadPoolExecutor(10, 20, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.info("Exception message - " + throwable.getMessage());
            log.info("Method name - " + method.getName());
            for (Object param : objects) {
                log.info("Parameter value - " + param);
            }
        }
    }

}
