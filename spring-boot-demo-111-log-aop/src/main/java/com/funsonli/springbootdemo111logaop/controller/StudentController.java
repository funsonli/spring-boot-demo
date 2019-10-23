package com.funsonli.springbootdemo111logaop.controller;

import com.funsonli.springbootdemo111logaop.annotation.BootanLog;
import com.funsonli.springbootdemo111logaop.entity.Student;
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

    @BootanLog(value = "index", type = 2)
    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @BootanLog(value = "view", type = 3)
    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        return "view " + id;
    }

}
