package com.funsonli.springbootdemo320elasticsearch.controller;

import com.funsonli.springbootdemo320elasticsearch.dao.StudentDao;
import com.funsonli.springbootdemo320elasticsearch.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    StudentDao studentDao;

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {

        List<Student> models = studentDao.findByName(id);
        return "ok " + models.toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        studentDao.save(model);
        return "ok";
    }

    @GetMapping("/view/{min}/{max}")
    public String view(HttpServletRequest request, @PathVariable Integer min, @PathVariable Integer max) {

        List<Student> models = studentDao.findByAgeBetween(min, max);
        return "ok " + models.toString();
    }

}
