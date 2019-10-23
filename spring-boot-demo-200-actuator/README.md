# Spring Boot入门样例-200-actuator监控工具

> 如何知道当前服务器运行状态。本demo演示如何使用actuator监控服务器状态。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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
management:
  health:
    status:
      http-mapping:
        DOWN: 200
        OUT_OF_SERVICE: 200
        FATAL: 200
        UNKNOWN: 200
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      base-path: /bootan/actuator/
      exposure:
        include: '*'
```

### 代码解析

无需代码

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/bootan/actuator
浏览器访问 http://localhost:8080/bootan/actuator/metrics

{"_links":{"self":{"href":"http://localhost:8080/bootan/actuator","templated":false},"beans":{"href":"http://localhost:8080/bootan/actuator/beans","templated":false},"caches-cache":{"href":"http://localhost:8080/bootan/actuator/caches/{cache}","templated":true},"caches":{"href":"http://localhost:8080/bootan/actuator/caches","templated":false},"health":{"href":"http://localhost:8080/bootan/actuator/health","templated":false},"health-path":{"href":"http://localhost:8080/bootan/actuator/health/{*path}","templated":true},"info":{"href":"http://localhost:8080/bootan/actuator/info","templated":false},"conditions":{"href":"http://localhost:8080/bootan/actuator/conditions","templated":false},"configprops":{"href":"http://localhost:8080/bootan/actuator/configprops","templated":false},"env":{"href":"http://localhost:8080/bootan/actuator/env","templated":false},"env-toMatch":{"href":"http://localhost:8080/bootan/actuator/env/{toMatch}","templated":true},"loggers":{"href":"http://localhost:8080/bootan/actuator/loggers","templated":false},"loggers-name":{"href":"http://localhost:8080/bootan/actuator/loggers/{name}","templated":true},"heapdump":{"href":"http://localhost:8080/bootan/actuator/heapdump","templated":false},"threaddump":{"href":"http://localhost:8080/bootan/actuator/threaddump","templated":false},"metrics":{"href":"http://localhost:8080/bootan/actuator/metrics","templated":false},"metrics-requiredMetricName":{"href":"http://localhost:8080/bootan/actuator/metrics/{requiredMetricName}","templated":true},"scheduledtasks":{"href":"http://localhost:8080/bootan/actuator/scheduledtasks","templated":false},"mappings":{"href":"http://localhost:8080/bootan/actuator/mappings","templated":false}}}

{"names":["jvm.memory.max","jvm.threads.states","jvm.gc.memory.promoted","jvm.memory.used","jvm.gc.max.data.size","jvm.gc.pause","jvm.memory.committed","system.cpu.count","logback.events","jvm.buffer.memory.used","tomcat.sessions.created","jvm.threads.daemon","system.cpu.usage","jvm.gc.memory.allocated","tomcat.sessions.expired","jvm.threads.live","jvm.threads.peak","process.uptime","tomcat.sessions.rejected","process.cpu.usage","jvm.classes.loaded","jvm.classes.unloaded","tomcat.sessions.active.current","tomcat.sessions.alive.max","jvm.gc.live.data.size","jvm.buffer.count","jvm.buffer.total.capacity","tomcat.sessions.active.max","process.start.time"]}

```

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- https://blog.csdn.net/jy02268879/article/details/84134634


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

