# Spring Boot入门样例-300-redis使用Redis缓存数据

> Redis是一款高性能内存Key Value键值对的Nosql数据库。本demo演示如何演示如何简单缓存和获取数据。

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java和Maven安装配置(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
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
  # Redis
  redis:
    # 单机版
    host: 127.0.0.1
    port: 6379
    password:
    # 数据库索引 默认0
    database: 0
    # 超时时间 Duration类型 3秒
    timeout: 3S
```

### 代码解析

SiteController.java 如下 view中存储数据，index方法中获取数据
``` 
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"", "/", "index"})
    public String index() {
        String id = redisTemplate.opsForValue().get("id");
        if (id != null) {
            return "view id: " + id;
        } else {
            return "please view id first. link: /student/view/1";
        }
    }

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {

        redisTemplate.opsForValue().set("id", id);
        return "ok";
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/view/2  
ok

浏览器访问 http://localhost:8080/student/
view id: 2

```

显示上次一访问 2

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

