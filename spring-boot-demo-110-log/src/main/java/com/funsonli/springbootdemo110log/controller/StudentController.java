package com.funsonli.springbootdemo110log.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @GetMapping({"", "/", "index"})
    public String index() {
        log.info("index");
        log.error("index error");
        return "index";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        log.info("view" + id);
        return "view " + id;
    }
}
