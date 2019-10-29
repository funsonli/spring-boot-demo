# Spring Boot入门样例-210-Admin监控工具

> 如何知道当前服务器或者其他服务器的运行状态。本demo演示如何使用Admin监控多台服务器状态。

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
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
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
server:
  port: 8080

spring:
  boot:
    admin:
      # 修改上下文路径，表示Admin服务器
      context-path: /bootan/admin
      # 表示客户端发送状态到所在的服务器
      client:
        url: http://127.0.0.1:${server.port}/bootan/admin

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
      # 健康情况always表示可显示硬盘使用情况和线程情况
      show-details: always
  endpoints:
    web:
      # 自定义访问地址
      base-path: /bootan/actuator/
      exposure:
        # 设置哪些可以访问，设置*表示所有，默认为["health","info"]
        include: '*'
```

### 代码解析

无需代码

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/bootan/admin

```

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-210-template-01.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-210-template-03.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- https://blog.csdn.net/jy02268879/article/details/84134634


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

