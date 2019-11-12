# Spring Boot入门样例-330-memcache整合memcache缓存数据

> memcache是一款高性能内存Key Value键值对的Nosql数据库。本demo演示如何演示如何简单缓存和获取数据。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> memcached 安装请参考 https://www.runoob.com/memcached/window-install-memcached.html

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.googlecode.xmemcached</groupId>
            <artifactId>xmemcached</artifactId>
            <version>2.4.5</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
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
memcached:
  servers: 127.0.0.1:11211
  poolSize: 10
  opTimeout: 6000
```

### 代码解析

MemcachedProperties.java 如下 
``` 
/**
 * yml配置
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "memcached")
public class MemcachedProperties {
    private String servers;
    private Integer poolSize;
    private Integer opTimeout;
}
```

MemcachedProperties.java 如下 Memcached Client 配置
```
/**
 * Memcached Client 配置
 *
 * @author Funsonli
 * @date 2019/11/12
 */
@Slf4j
@Configuration
public class MemcachedConfig {

    @Resource
    private MemcachedProperties memcachedProperties;

    @Bean
    public MemcachedClient getMemcachedClinet(){
        MemcachedClient memcachedClient = null;
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(memcachedProperties.getServers()));
            builder.setConnectionPoolSize(memcachedProperties.getPoolSize());
            builder.setOpTimeout(memcachedProperties.getOpTimeout());
            memcachedClient = builder.build();
        }catch (IOException e){
            log.error("init MemcachedClient failed" + e);
        }
        return memcachedClient;
    }
}

```

SiteController.java 如下 view中存储数据，index方法中获取数据
``` 
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private MemcachedClient memcachedClient;

    @GetMapping({"", "/", "index"})
    public String index() {
        try {
            String id = memcachedClient.get("id");
            if (id != null) {
                return "view id: " + id;
            } else {
                return "please view id first. link: /student/view/1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "please view id first. link: /student/view/1";
    }

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {
        try {
            memcachedClient.set("id", 0, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
控制台启动日志， 生成10个线程池
2019-11-12 11:11:25.024  WARN 108940 --- [           main] net.rubyeye.xmemcached.XMemcachedClient  : You are using connection pool for xmemcached client,it's not recommended unless you have test it that it can boost performance in your app.
2019-11-12 11:11:25.039  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.048  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.057  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.063  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.068  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.073  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.082  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.086  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.092  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211
2019-11-12 11:11:25.097  INFO 108940 --- [ached-Reactor-0] c.g.c.y.core.impl.AbstractController     : Add a session: 127.0.0.1:11211


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
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

