# Spring Boot入门样例-301-redis-cache使用Redis Caching缓存数据

> Redis是一款高性能内存Key Value键值对的Nosql数据库。本demo演示如何演示如何使用@CachePut 和 @Cacheable缓存数据。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

相关功能
- 如果想查看Ehcache做缓存，请查看[Spring Boot入门样例-120-ehcache-cache整合Ehcache Caching缓存数据](https://github.com/funsonli/spring-boot-demo/tree/master/spring-boot-demo-120-ehcache-cache)
- 如果想查看Guava做缓存，请查看[Spring Boot入门样例-230-guava-cache整合Guava Caching缓存数据](https://github.com/funsonli/spring-boot-demo/tree/master/spring-boot-demo-230-guava-cache)
- 如果想查看Redis做缓存，请查看[Spring Boot入门样例-301-redis-cache使用Redis Caching缓存数据](https://github.com/funsonli/spring-boot-demo/tree/master/spring-boot-demo-301-redis-cache)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
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
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-active: 6
        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-wait: -1ms
        # 连接池中的最大空闲连接 默认 8
        max-idle: 6
        # 连接池中的最小空闲连接 默认 0
        min-idle: 0

  cache:
    type: redis


  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
    username: root
    password: root

  jpa:
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL55Dialect

```

### 代码解析

其他代码解析参考 spring-boot-demo-040-jpa 模块。

StudentServiceImpl.java 如下 @CachePut 和 @Cacheable都会将数据缓存到Redis中
``` 
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> index() {
        return studentDao.findAll();
    }

    @Override
    @CachePut(value = "student", key = "#student.id")
    public Student save(Student student) {
        log.info("inser to mysql: " + student.getId());
        return studentDao.save(student);
    }

    @Override
    @Cacheable(value = "student", key = "#id")
    public Optional<Student> findById(String id) {
        log.info("find from mysql: " + id);
        return studentDao.findById(id);
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson, age=30), Student(id=381159203135926560, name=zhonghua, age=26), Student(id=382624532634144768, name=funson, age=30), Student(id=382624644089384960, name=zhonghua, age=28), Student(id=384105379728068608, name=jack, age=25)]

浏览器访问 http://localhost:8080/student/view/381159203135426560
控制台日志: find from mysql: 381159203135426560  表示从数据库里查询

从redis客户端执行 keys * 可以看到"student::381159203135426560"

浏览器访问 http://localhost:8080/student/view/381159203135426560
没有日志，表示从redis缓存中读取


浏览器访问 http://localhost:8080/student/add/funsonli1/29
Student(id=384426023674056704, name=funsonli1, age=29)

从redis客户端执行 keys * 可以看到"student::384426023674056704"

浏览器访问 http://localhost:8080/student/view/384426023674056704


浏览器访问 http://localhost:8080/student/delete/384426023674056704
从redis客户端执行 keys * 找不到"student::384426023674056704"
```


### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

