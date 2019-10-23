package com.funsonli.springbootdemo305redissentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"", "/", "index"})
    public String index() {
        String id = redisTemplate.opsForValue().get("id");
        if (id != null) {
            return "view id: " + id;
        } else {
            return "please view id first. link: /student/view/1";
        }
    }

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {

        redisTemplate.opsForValue().set("id", id);
        return "ok";
    }
}
