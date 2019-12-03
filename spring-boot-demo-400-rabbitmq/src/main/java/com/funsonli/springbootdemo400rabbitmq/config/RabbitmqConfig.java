package com.funsonli.springbootdemo400rabbitmq.config;

import com.funsonli.springbootdemo400rabbitmq.constant.RabbitmqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author i
 * @date 2019/10/8
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

    /**
     * Direct模式消息队列
     *
     * @author Funson
     * @date 2019/10/15
     * @param
     * @return Queue
     */
    @Bean
    public Queue directQueue() {
        return new Queue(RabbitmqConstant.DIRECT_QUEUE);
    }

    /**
     * Fanout模式消息队列，定义3个队列，1个Exchange，再将3个队列绑定到1个Exchange
     *
     * @author Funson
     * @date 2019/10/15
     * @param
     * @return Queue
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(RabbitmqConstant.FANOUT_QUEUE_1);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(RabbitmqConstant.FANOUT_QUEUE_2);
    }

    @Bean
    public Queue fanoutQueue3() {
        return new Queue(RabbitmqConstant.FANOUT_QUEUE_3);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitmqConstant.FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }

    /**
     * 规则1 fanout.# 可以把fanout交换机绑定进来
     * 规则2 绑定单个队列
     * 规则3 绑定
     * 符号“#”匹配一个或多个词 比如“hello.#”能够匹配到“hello.123.456”
     * 符号“*”匹配一个词 “hello.*”只能匹配到“hello.123”
     *
     * @author Funson
     * @date 2019/10/15
     * @return TopicExchange
     */

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitmqConstant.TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(FanoutExchange fanoutExchange, TopicExchange topicExchange) {
        return BindingBuilder.bind(fanoutExchange).to(topicExchange).with(RabbitmqConstant.TOPIC_RULE_1);
    }

    @Bean
    public Binding topicBinding2(Queue fanoutQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(topicExchange).with(RabbitmqConstant.TOPIC_RULE_2);
    }

    @Bean
    public Binding topicBinding3(Queue fanoutQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(topicExchange).with(RabbitmqConstant.TOPIC_RULE_3);
    }

    @Bean
    public Binding topicBinding4(Queue directQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(directQueue).to(topicExchange).with(RabbitmqConstant.TOPIC_RULE_3);
    }

}
