package com.funsonli.springbootdemo160task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootDemo160TaskApplication {

    public static void main(String[] args) {
        System.out.println("main thread " + Thread.currentThread().getName());
        SpringApplication.run(SpringBootDemo160TaskApplication.class, args);
    }

}
