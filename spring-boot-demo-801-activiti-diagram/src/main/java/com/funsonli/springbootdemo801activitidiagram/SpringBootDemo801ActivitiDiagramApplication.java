package com.funsonli.springbootdemo801activitidiagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class SpringBootDemo801ActivitiDiagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo801ActivitiDiagramApplication.class, args);
    }

}
