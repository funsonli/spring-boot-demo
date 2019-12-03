package com.funsonli.springbootdemo400rabbitmq.constant;

/**
 * @author i
 * @date 2019/10/8
 */
public class RabbitmqConstant {

    public static final String DIRECT_QUEUE = "direct.queue.1";

    public static final String FANOUT_QUEUE_1 = "fanout.queue.1";
    public static final String FANOUT_QUEUE_2 = "fanout.queue.2.2";
    public static final String FANOUT_QUEUE_3 = "fanout.queue.3";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String TOPIC_RULE_1 = "fanout.queue.*";
    public static final String TOPIC_RULE_2 = "fanout.queue.2.2";
    public static final String TOPIC_RULE_3 = "#.1";
}
