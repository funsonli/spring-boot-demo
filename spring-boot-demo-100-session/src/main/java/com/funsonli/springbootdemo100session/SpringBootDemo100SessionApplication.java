package com.funsonli.springbootdemo100session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class SpringBootDemo100SessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo100SessionApplication.class, args);
    }

}
