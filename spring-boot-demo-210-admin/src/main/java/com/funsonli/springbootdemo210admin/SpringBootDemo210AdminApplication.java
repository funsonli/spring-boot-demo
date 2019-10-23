package com.funsonli.springbootdemo210admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringBootDemo210AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo210AdminApplication.class, args);
    }

}
