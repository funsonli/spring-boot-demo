package com.funsonli.springbootdemo120ehcachecache.controller;

import com.funsonli.springbootdemo120ehcachecache.entity.Student;
import com.funsonli.springbootdemo120ehcachecache.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
        return studentService.index().toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        model = studentService.save(model);
        return model.toString();
    }


    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        Optional<Student> optionalStudent = studentService.findById(id);
        if (optionalStudent.isPresent()) {
            return optionalStudent.get().toString();
        }

        return "null";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        studentService.deleteById(id);
        return "ok";
    }
}
