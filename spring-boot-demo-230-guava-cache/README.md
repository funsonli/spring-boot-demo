# Spring Boot入门样例-230-guava-cache整合Guava Caching缓存数据

> 每次都读取数据库非常慢，对于相同的查询，我们可以使用缓存。本demo演示如何使用guava和@CachePut 和 @Cacheable缓存数据。

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
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>25.1-jre</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>4.3.7.RELEASE</version>
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

GuavaConfig.java 如下
```
@Configuration
public class GuavaConfig {

    /**
     * spring缓存配置，使用guava
     * @author Funson
     * @date 2019/10/24
     * @return
     */
    @Bean
    public CacheManager cacheManager(){
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        // 设置过期时间为7200秒
        cacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(7200, TimeUnit.SECONDS));
        return cacheManager;
    }
}
```
StudentServiceImpl.java 如下 @CachePut 和 @Cacheable都会将数据缓存到guava中
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

SpringBootDemo230GuavaCacheApplication.java  要加上@EnableCaching
```
@SpringBootApplication
@EnableCaching
public class SpringBootDemo230GuavaCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo230GuavaCacheApplication.class, args);
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson, age=30), Student(id=381159203135926560, name=zhonghua, age=26), Student(id=382624532634144768, name=funson, age=30), Student(id=382624644089384960, name=zhonghua, age=28), Student(id=384105379728068608, name=jack, age=25)]

浏览器访问 http://localhost:8080/student/view/381159203135426560
控制台日志: 
2019-10-24 09:51:38.130  INFO 12920 --- [nio-8080-exec-1] c.f.s.service.impl.StudentServiceImpl    : find from mysql: 381159203135426560
Hibernate: select student0_.id as id1_0_0_, student0_.age as age2_0_0_, student0_.name as name3_0_0_ from student student0_ where student0_.id=?
表示从数据库里查询


浏览器再次访问 http://localhost:8080/student/view/381159203135426560
没有日志，表示从guava缓存中读取


浏览器访问 http://localhost:8080/student/add/funsonli1/29
Student(id=384426023674056704, name=funsonli1, age=29)

浏览器访问 http://localhost:8080/student/view/384426023674056704


浏览器访问 http://localhost:8080/student/delete/384426023674056704
清除缓存
```


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

