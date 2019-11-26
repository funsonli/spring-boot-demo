package com.funsonli.springbootdemo154mybatisatomikos.controller;

import com.funsonli.springbootdemo154mybatisatomikos.entity.Order;
import com.funsonli.springbootdemo154mybatisatomikos.entity.Student;
import com.funsonli.springbootdemo154mybatisatomikos.service.AddService;
import com.funsonli.springbootdemo154mybatisatomikos.service.OrderService;
import com.funsonli.springbootdemo154mybatisatomikos.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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
    AddService addService;

    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        addService.add(name);
        return "ok";
    }

}
