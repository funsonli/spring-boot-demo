package com.funsonli.springbootdemo161quartz.controller;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    // 此处为字符串，也可以从数据表中读取出来
    private static final String jobName = "com.funsonli.springbootdemo161quartz.job.SampleJob";
    private static final String cronExpression = "0/3 * * * * ?";
    private static final String parameter = "param";

    @Autowired
    private Scheduler scheduler;

    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping({"add"})
    public String add() {
        addJob(jobName);
        return "add job";
    }

    @GetMapping({"pause"})
    public String pause() {
        pauseJob(jobName);
        return "pause job";
    }

    @GetMapping({"resume"})
    public String resume() {
        resumeJob(jobName);
        return "resume job";
    }

    @GetMapping({"delete"})
    public String delete() {
        deleteJob(jobName);
        return "delete job";
    }

    private void addJob(String jobClassName) {
        try {
            // 启动调度器
            scheduler.start();

            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName)
                    .usingJobData("parameter", parameter)
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

    private void pauseJob(String jobClassName) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobClassName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void resumeJob(String jobClassName) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobClassName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(String jobClassName){

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Job getClass(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        return (Job)clazz.newInstance();
    }

}
