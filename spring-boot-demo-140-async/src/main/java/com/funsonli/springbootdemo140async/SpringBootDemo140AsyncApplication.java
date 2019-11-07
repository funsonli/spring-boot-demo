package com.funsonli.springbootdemo140async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootDemo140AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo140AsyncApplication.class, args);
    }

}
