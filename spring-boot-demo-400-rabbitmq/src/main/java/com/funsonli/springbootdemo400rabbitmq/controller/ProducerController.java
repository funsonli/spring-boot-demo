package com.funsonli.springbootdemo400rabbitmq.controller;

import com.funsonli.springbootdemo400rabbitmq.constant.RabbitmqConstant;
import com.funsonli.springbootdemo400rabbitmq.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RabbitTemplate rabbitTemplate;

    /**
     * fanout 一个入，一个出 1:1
     * @author Funsonli
     * @date 2019/10/3
     */
    @GetMapping("/direct")
    public String direct() {
        rabbitTemplate.convertAndSend(RabbitmqConstant.DIRECT_QUEUE, new Message(id.getAndIncrement(), "direct"));
        return "direct success";
    }

    /**
     * fanout 一个入，多个出 1:N
     * @author Funsonli
     * @date 2019/10/3
     */
    @GetMapping("/fanout")
    public String fanout() {
        rabbitTemplate.convertAndSend(RabbitmqConstant.FANOUT_EXCHANGE, "", new Message(id.getAndIncrement(), "fanout"));
        return "fanout success";
    }

    /**
     * fanout 一个入，多个出 根据规则匹配
     * 规则1 fanout.# 可以把fanout交换机绑定进来
     * 规则2 绑定单个队列
     * 规则3 绑定
     * 符号“#”匹配一个或多个词 比如“hello.#”能够匹配到“hello.123.456”
     * 符号“*”匹配一个词 “hello.*”只能匹配到“hello.123”
     * @author Funsonli
     * @date 2019/10/3
     */
    @GetMapping("/topic1")
    public String topic1() {
        rabbitTemplate.convertAndSend(RabbitmqConstant.TOPIC_EXCHANGE, "fanout.queue.k", new Message(id.getAndIncrement(), "topic1"));
        return "topic1 success";
    }

    @GetMapping("/topic2")
    public String topic2() {
        rabbitTemplate.convertAndSend(RabbitmqConstant.TOPIC_EXCHANGE, "fanout.queue.2.2", new Message(id.getAndIncrement(), "topic2"));
        return "topic2 success";
    }

    @GetMapping("/topic3")
    public String topic3() {
        rabbitTemplate.convertAndSend(RabbitmqConstant.TOPIC_EXCHANGE, "abc.queue.1", new Message(id.getAndIncrement(), "topic3"));
        return "topic3 success";
    }
}
