package com.funsonli.springbootdemo161quartz.job;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/11/8
 */
@Data
@Slf4j
public class SampleJob implements Job {

    private String parameter;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("欢迎使用Spring Boot Demo by funsonli " + parameter);
    }
}
