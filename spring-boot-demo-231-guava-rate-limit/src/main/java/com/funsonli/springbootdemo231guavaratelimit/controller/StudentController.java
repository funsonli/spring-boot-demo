package com.funsonli.springbootdemo231guavaratelimit.controller;

import com.funsonli.springbootdemo231guavaratelimit.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @RateLimiter(limit = 1, timeout = 500)
    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @RateLimiter(limit = 3, timeout = 1000)
    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        return "view " + id;
    }

}
