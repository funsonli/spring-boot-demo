# Spring Boot入门样例-400-rabbitmq整合Rabbitmq消息队列

> 当用户注册成功后，需要给用户发送邮件或短信，但是这种操作不需要实时，可以发送到消息队列中，由专门的系统完成。本demo演示如何使用Rabbitmq消息队列。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> rabbitmq 安装请参考 https://www.jianshu.com/p/3d43561bb3ee

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
spring:
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
    #    指明采用发送者确认模式
    publisher-confirms: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: auto
      direct:
        acknowledge-mode: auto
```

### 代码解析

RabbitmqConfig.java 如下 
``` 
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
```

DirectQueueConsumer.java 如下 直接模式
```
@Component
@RabbitListener(queues = RabbitmqConstant.DIRECT_QUEUE)
public class DirectQueueConsumer {
    @RabbitHandler
    public void process(Message message) {
        System.out.println("收到消息Direct: " + message.toString());
    }
}

```

Fanout1Consumer.java如下，其他两个类似
``` 
@Component
@RabbitListener(queues = RabbitmqConstant.FANOUT_QUEUE_1)
public class Fanout1Consumer {
    @RabbitHandler
    public void process(Message message) {
        System.out.println("收到消息fanout1: " + message.toString());
    }
}
```

ProducerController.java 如下 view中存储数据，index方法中获取数据
``` 
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

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

浏览器访问 http://localhost:8080/direct

```
2019-10-15 15:26:09.580  INFO 6568 --- [ 127.0.0.1:5672] c.f.s.config.RabbitmqConfig              : 消息发送成功:correlationData(null),ack(true),cause(null)
收到消息Direct: Message(id=0, message=direct)
```


浏览器访问 http://localhost:8080/fanout

```
2019-10-15 15:51:06.487  INFO 7576 --- [ 127.0.0.1:5672] c.f.s.config.RabbitmqConfig              : 消息发送成功:correlationData(null),ack(true),cause(null)
收到消息fanout1: Message(id=0, message=fanout)
收到消息fanout2: Message(id=0, message=fanout)
收到消息fanout3: Message(id=0, message=fanout)
```

浏览器访问 http://localhost:8080/topic1

```
2019-10-15 16:13:59.732  INFO 9736 --- [ 127.0.0.1:5672] c.f.s.config.RabbitmqConfig              : 消息发送成功:correlationData(null),ack(true),cause(null)
收到消息fanout1: Message(id=1, message=topic1)
收到消息fanout3: Message(id=1, message=topic1)
收到消息fanout2: Message(id=1, message=topic1)
```

浏览器访问 http://localhost:8080/topic2
```
2019-10-15 16:14:19.399  INFO 9736 --- [ 127.0.0.1:5672] c.f.s.config.RabbitmqConfig              : 消息发送成功:correlationData(null),ack(true),cause(null)
收到消息fanout2: Message(id=0, message=topic2)
```

浏览器访问 http://localhost:8080/topic3
```
2019-10-15 16:18:44.968  INFO 6152 --- [ 127.0.0.1:5672] c.f.s.config.RabbitmqConfig              : 消息发送成功:correlationData(null),ack(true),cause(null)
收到消息Direct: Message(id=3, message=topic3)
收到消息fanout1: Message(id=3, message=topic3)
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

