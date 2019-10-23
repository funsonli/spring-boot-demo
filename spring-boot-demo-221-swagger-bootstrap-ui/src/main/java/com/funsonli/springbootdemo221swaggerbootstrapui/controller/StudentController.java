package com.funsonli.springbootdemo221swaggerbootstrapui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@Slf4j
@RestController
@RequestMapping("/student")
@Api("学生管理")
public class StudentController {

    @ApiOperation("学生列表")
    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @ApiOperation(value = "查看学生", notes = "单个学生详情")
    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        return "view " + id;
    }

}
