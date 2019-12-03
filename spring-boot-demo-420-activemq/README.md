# Spring Boot入门样例-420-activemq整合Activemq消息队列

> 当用户注册成功后，需要给用户发送邮件或短信，但是这种操作不需要实时，可以发送到消息队列中，由专门的系统完成。本demo演示如何使用Activemq消息队列。

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
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-broker</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
    pool:
      enabled: false
```

### 代码解析

RabbitmqConfig.java 如下 
``` 
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

```

ProducerController.java 如下 view中存储数据，index方法中获取数据
``` 
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

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

浏览器访问 http://localhost:8080/send

浏览器访问 http://localhost:8080/json

控制台显示：

```
receive: hello activemq funson
receive: hello json funson
```

active 可以通过查看状态

http://localhost:8161/admin/index.jsp 帐号和密码都是admin admin
 

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

