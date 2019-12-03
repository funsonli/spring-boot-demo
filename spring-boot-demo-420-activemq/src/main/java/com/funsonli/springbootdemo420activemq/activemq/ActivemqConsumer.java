package com.funsonli.springbootdemo420activemq.activemq;

import com.alibaba.fastjson.JSON;
import com.funsonli.springbootdemo420activemq.vo.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/15
 */
@Component
public class ActivemqConsumer {
    @JmsListener(destination = "springboot.funsonli.test")
    public void process(String message) {
        System.out.println("receive: " + message);
    }

    /**
     * 推荐使用json格式，因为其他格式容易出现解码问题
     * @author Funson
     * @date 2019/10/16
     */
    @JmsListener(destination = "springboot.funsonli.json")
    public void json(String str) {
        Message message = JSON.parseObject(str, Message.class);
        System.out.println("receive: " + message.getMessage());
    }
}
