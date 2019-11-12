package com.funsonli.springbootdemo800activiti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class SpringBootDemo800ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo800ActivitiApplication.class, args);
    }

}
