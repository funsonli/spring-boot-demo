package com.funsonli.springbootdemo140async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/11/7
 */
@Slf4j
@Service
public class StudentService {

    /**
     *  无参数 无返回值
     * @author Funsonli
     * @date 2019/11/7
     */
    @Async
    public void async1() {
        try {
            Thread.sleep(5 * 1000);
            log.info("async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  有参数 无返回值
     * @author Funsonli
     * @date 2019/11/7
     * @param s :
     * @return : null
     */
    @Async
    public void async2(String s) {
        try {
            Thread.sleep(5 * 1000);
            log.info(s + " async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  有参数 有返回值
     * @author Funsonli
     * @date 2019/11/7
     * @param s :
     * @return : Future
     */
    @Async
    public Future async3(String s) {
        Future future;
        try {
            Thread.sleep(5 * 1000);
            log.info(s + "async funson " + Thread.currentThread().getName());
            future = new AsyncResult(s + "async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            future = new AsyncResult(s + " error " + Thread.currentThread().getName());
            e.printStackTrace();
        }
        return future;
    }

}
