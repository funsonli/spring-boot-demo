package com.funsonli.springbootdemo160task.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/11/8
 */
@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    /**
     * 设置定时任务线程池，设置10个名称为 task-开头的线程
     * @author Funsonli
     * @date 2019/11/8
     * @return : Executor
     */
    @Bean
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(10, new ThreadFactoryBuilder().setNamePrefix("task-").build());
    }
}
