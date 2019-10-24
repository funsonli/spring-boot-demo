package com.funsonli.springbootdemo230guavacache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootDemo230GuavaCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo230GuavaCacheApplication.class, args);
    }

}
