package com.funsonli.springbootdemo010helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootDemo010HelloWorldApplication {

    /**
     * 浏览器输入 http://localhost:8080  可以看到 hello, world
     * @author Funson
     * @date 2019/10/13
     * @return String
     */
    @GetMapping("/")
    public String index() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo010HelloWorldApplication.class, args);
    }

}
