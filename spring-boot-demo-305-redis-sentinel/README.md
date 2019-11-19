# Spring Boot入门样例-305-redis-sentinel整合Redis哨兵模式

> Redis哨兵模式能保障Redis服务器高可用，当主节点宕机时对用户不产生影响。本demo演示如何设置和配置Redis Sentinel哨兵模式。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### 准备工作
启动3个redis节点，1主2备，启动3个哨兵节点

```
# redis-6379.conf
bind 127.0.0.1
port 6379
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 2

# redis-6380.conf
bind 127.0.0.1
port 6380
slaveof 127.0.0.1 6379

# redis-6381.conf
bind 127.0.0.1
port 6381
slaveof 127.0.0.1 6379

# sentinel26379.conf
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 15000
sentinel config-epoch mymaster 3

# sentinel26380.conf
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 15000
sentinel config-epoch mymaster 3

# sentinel26381.conf
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 15000
sentinel config-epoch mymaster 3

```

在命令行模式下分别启动redis和哨兵

```
D:\redis>redis-server.exe redis-6379.conf
D:\redis>redis-server.exe redis-6380.conf
D:\redis>redis-server.exe redis-6381.conf
D:\redis>redis-server.exe sentinel26379.conf  --sentinel
D:\redis>redis-server.exe sentinel26380.conf  --sentinel
D:\redis>redis-server.exe sentinel26381.conf  --sentinel
```

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
    password:
    # 数据库索引 默认0
    database: 0
    # 超时时间 Duration类型 3秒
    timeout: 3S
    cluster:
      nodes:
        - 127.0.0.1:6379
        - 127.0.0.1:6380
        - 127.0.0.1:6381
    sentinel:
      master: mymaster
      nodes: 127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381
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

一直刷新http://localhost:8080/student/，同时推出6379redis服务器。发现一直都能获取到数据

D:\redis>redis-server.exe sentinel26379.conf  --sentinel
[7820] 23 Oct 17:39:04.675 # Sentinel runid is dab21d769fc5a5d68a388dac0e6925c062dc615d
[7820] 23 Oct 17:39:04.676 # +monitor master mymaster 127.0.0.1 6380 quorum 2
[7820] 23 Oct 17:39:09.713 # +sdown sentinel 127.0.0.1:26381 127.0.0.1 26381 @ mymaster 127.0.0.1 6380
[7820] 23 Oct 17:39:15.713 * +convert-to-slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
[7820] 23 Oct 17:40:47.071 * +sentinel sentinel 127.0.0.1:26380 127.0.0.1 26380 @ mymaster 127.0.0.1 6380
[7820] 23 Oct 17:40:58.936 * -dup-sentinel master mymaster 127.0.0.1 6380 #duplicate of 127.0.0.1:26381 or 33b592f5efeac5a8463996334b4fcc11a59b3880
[7820] 23 Oct 17:40:58.936 * +sentinel sentinel 127.0.0.1:26381 127.0.0.1 26381 @ mymaster 127.0.0.1 6380
[7820] 23 Oct 17:47:55.100 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380

最后一行，6379端口的Redis宕机之后，6380经过投票做为master节点。

[7820] 23 Oct 17:49:35.806 # -sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
[7820] 23 Oct 17:49:45.752 * +convert-to-slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380

6379节点再次启动后，也不会作为master节点，而是作为6380的slave

```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

