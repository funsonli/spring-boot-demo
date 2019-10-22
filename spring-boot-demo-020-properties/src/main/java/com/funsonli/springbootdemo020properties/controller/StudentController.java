package com.funsonli.springbootdemo020properties.controller;

import com.funsonli.springbootdemo020properties.controller.config.properties.StudentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    StudentProperties studentProperties;

    @GetMapping({"/", "index"})
    public String index() {
        return "student name: " + studentProperties.getName() + " and age: " + studentProperties.getAge();
    }

}
