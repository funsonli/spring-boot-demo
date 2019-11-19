# Spring Boot入门样例-100-session使用Session识别用户

> 用户访问商城时也可以将商品添加到购物车，并且跳转到其他页面时购物车的信息依然存在。本demo演示如何演示用户未登录情况下使用Session保存用户添加购物车。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

相关功能
- 如果想查看Redis功能，请查看[Spring Boot入门样例-301-redis-cache使用Redis](https://github.com/funsonli/spring-boot-demo/tree/master/spring-boot-demo-300-redis)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

### 代码解析

SpringBootDemo100SessionApplication.java 如下 添加启用注解@EnableRedisHttpSession
``` 
@EnableRedisHttpSession
@SpringBootApplication
public class SpringBootDemo100SessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo100SessionApplication.class, args);
    }

}

```

StudentController.java 如下 添加启用注解@EnableRedisHttpSession
``` 
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping({"", "/", "index"})
    public String index(HttpSession session) {

        Object value = session.getAttribute("guest");

        return "guest=" + value.toString();
    }

    @GetMapping("/add/{product}/{number}")
    public String add(HttpServletRequest request, @PathVariable String product, @PathVariable Integer number, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("JSESSION")) {
                    log.info(cookie.getName() + "=" + cookie.getValue());
                }
            }
        }

        Object oldValue = session.getAttribute("guest");
        Object value = "{product: '" + product + "', number: " + number + "}";
        session.setAttribute("guest", "{product: '" + product + "', number: " + number + "}");

        return "add success, 请刷新或者访问 /index value: " + value + " old value: " + oldValue;
    }
}


```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8088/student/add/phone/3 第一次访问会添加购物车，没有旧值
add success, 请刷新或者访问 /index value: {product: 'phone', number: 3} old value:null


浏览器访问 http://localhost:8088/student/add/phone/3  第二次之后有旧值
add success, 请刷新或者访问 /index value: {product: 'phone', number: 5} old value: {product: 'phone', number: 3}


控制台可以看
2019-11-12 18:48:05.274  INFO 175896 --- [nio-8088-exec-1] c.f.s.controller.StudentController       : JSESSIONID=2C450A6B12C36626AD7FB83A4E62F6DE

redis-cli控制台可以看到
127.0.0.1:6379> hgetall spring:session:sessions:ccfbc4fc-4929-4996-b3db-bbb5d3cc1f36
1) "creationTime"
2) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01n_)\xbf\xa3"
3) "sessionAttr:guest"
4) "\xac\xed\x00\x05t\x00\x1d{product: 'phone', number: 3}"
5) "maxInactiveInterval"
6) "\xac\xed\x00\x05sr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\a\b"
7) "lastAccessedTime"
8) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01n_-\xf6\x88"

```

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-100-01.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

