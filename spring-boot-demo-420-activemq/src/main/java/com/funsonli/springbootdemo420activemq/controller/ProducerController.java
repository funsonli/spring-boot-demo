package com.funsonli.springbootdemo420activemq.controller;

import com.alibaba.fastjson.JSON;
import com.funsonli.springbootdemo420activemq.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */
@Slf4j
@RestController
public class ProducerController {
    AtomicInteger id = new AtomicInteger();

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/send")
    public String send() {
        jmsTemplate.convertAndSend("springboot.funsonli.test", "hello activemq funson");
        return "send success";
    }

    @GetMapping("/json")
    public String json() {
        Message message = new Message(2, "hello json funson");
        jmsTemplate.convertAndSend("springboot.funsonli.json", JSON.toJSONString(message));
        return "send success";
    }
}
