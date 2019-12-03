package com.funsonli.springbootdemo400rabbitmq.rabbitmq;

import com.funsonli.springbootdemo400rabbitmq.constant.RabbitmqConstant;
import com.funsonli.springbootdemo400rabbitmq.vo.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/15
 */
@Component
@RabbitListener(queues = RabbitmqConstant.FANOUT_QUEUE_2)
public class Fanout2Consumer {
    @RabbitHandler
    public void process(Message message) {
        System.out.println("收到消息fanout2: " + message.toString());
    }
}
