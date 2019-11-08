package com.funsonli.springbootdemo160task.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/11/8
 */
@Slf4j
@Component
public class SampleTask {
    /**
     * 和linux cron类似，每5秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        log.info("【task1】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

    /**
     * 每10秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(fixedRate = 10000)
    public void task2() {
        log.info("【task2】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

    /**
     * 延迟5秒，每隔6秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(fixedDelay = 6000, initialDelay = 5000)
    public void task3() {
        log.info("【task3】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

}
