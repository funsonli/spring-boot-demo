# Spring Boot入门样例-010-hello-world

> 本demo用spring boot在网页上输出一个简单的hello world

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java安装(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
其中spring-boot-starter-web是必须的，具体参见该项目的pox.xml
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
```

### HelloWorldApplication.java

```java
/**
 * spring boot hello world
 *
 * @author Funson
 * @date 2019/10/12
 */
@SpringBootApplication
@RestController
public class HelloWorldApplication {

    /**
     * 浏览器输入 http://localhost:8080  可以看到 hello, world
     * @author Funson
     * @date 2019/10/13
     * @return String
     */
    @GetMapping("/")
    public String index() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }
}
```

- @RestController 表示输出restful字符串
- @GetMapping 表示浏览器使用GET方法获取数据

实际添加的代码仅有

```
    @GetMapping("/")
    public String index() {
        return "hello world";
    }
```

点击运行，浏览器输入 http://localhost:8080  可以看到 hello, world


### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

