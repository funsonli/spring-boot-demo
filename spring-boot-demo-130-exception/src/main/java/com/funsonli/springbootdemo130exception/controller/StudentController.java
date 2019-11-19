package com.funsonli.springbootdemo130exception.controller;

import com.funsonli.springbootdemo130exception.exception.BootanException;
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

    @GetMapping({"", "/", "index"})
    public String index() {
        throw new BootanException("index error");
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id) throws Exception {

        int i = 3 / id;
        return id + " id divide by 3 ";
    }
}
