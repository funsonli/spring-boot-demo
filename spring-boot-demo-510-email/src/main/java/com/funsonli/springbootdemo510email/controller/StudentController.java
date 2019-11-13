package com.funsonli.springbootdemo510email.controller;

import com.funsonli.springbootdemo510email.util.EmailUtil;
import com.funsonli.springbootdemo510email.vo.Message;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    EmailUtil emailUtil;

    @GetMapping({"", "/", "index"})
    public String index() {
        emailUtil.sendEmailText("funsonli@qq.com", "content", "content");
        return "send funson success";
    }

    @GetMapping("/html")
    public String html() {
        Message message = new Message(5, "funson");

        emailUtil.sendEmailHtml("funsonli@qq.com", "hello", "register-ok", message);
        return "send funson html success";
    }

}
