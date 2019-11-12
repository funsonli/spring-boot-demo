package com.funsonli.springbootdemo380hadoop.controller;

import com.funsonli.springbootdemo380hadoop.component.HadoopTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    HadoopTemplate hadoopTemplate;

    @GetMapping({"", "/", "index"})
    public String index() {
        StringBuilder sb = new StringBuilder();
        String[] list = hadoopTemplate.listFile("/");
        if (list.length > 0) {
            for (String str : list) {
                sb.append(str + "<br>");
            }
        } else {
            sb.append("nothing");
        }

        return sb.toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        hadoopTemplate.uploadFile("D:\\funson.txt");
        hadoopTemplate.download("/funson.txt", "D:\\temp\\");
        return "ok";
    }

}
