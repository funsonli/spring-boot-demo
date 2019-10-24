package com.funsonli.springbootdemo280docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootDemo280DockerApplication {

    @GetMapping("/")
    public String index() {
        return "hello docker";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo280DockerApplication.class, args);
    }

}
