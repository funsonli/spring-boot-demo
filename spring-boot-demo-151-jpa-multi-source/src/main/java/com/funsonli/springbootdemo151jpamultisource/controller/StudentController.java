package com.funsonli.springbootdemo151jpamultisource.controller;

import com.funsonli.springbootdemo151jpamultisource.entity.order.Order;
import com.funsonli.springbootdemo151jpamultisource.entity.user.Student;
import com.funsonli.springbootdemo151jpamultisource.service.OrderService;
import com.funsonli.springbootdemo151jpamultisource.service.StudentService;
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
    StudentService studentService;

    @Autowired
    OrderService orderService;

    @GetMapping({"", "/", "index"})
    public String index() {
        return studentService.index().toString() + " | " + orderService.index().toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        model = studentService.save(model);
        return model.toString();
    }

    @GetMapping("/order/{studentId}/{amount}")
    public String order(HttpServletRequest request, @PathVariable String studentId, @PathVariable Integer amount) {
        Order model = new Order();
        model.setStudentId(studentId);
        model.setSn(UUID.randomUUID().toString().replace("-", ""));
        model.setAmount(amount);

        model = orderService.save(model);
        return model.toString();
    }
}
