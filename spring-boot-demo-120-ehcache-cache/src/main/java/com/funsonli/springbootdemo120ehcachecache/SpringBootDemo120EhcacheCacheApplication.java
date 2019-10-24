package com.funsonli.springbootdemo120ehcachecache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootDemo120EhcacheCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo120EhcacheCacheApplication.class, args);
    }

}
