package com.funsonli.springbootdemo140async.controller;

import com.funsonli.springbootdemo140async.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

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

    @Autowired
    StudentService studentService;

    @GetMapping({"", "/", "index"})
    public String index() {
        studentService.async1();
        studentService.async2("2 ");
        Future future = studentService.async3("3 ");
        log.info("do first " + Thread.currentThread().getName());
        try {
            log.info(future.get().toString() + " back");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index funson " + Thread.currentThread().getName();
    }

}
