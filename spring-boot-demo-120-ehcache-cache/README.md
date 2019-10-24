# Spring Boot入门样例-301-ehcache-cache整合Ehcache Caching缓存数据

> 每次都读取数据库非常慢，对于相同的查询，我们可以使用缓存。本demo演示如何使用ehcache和@CachePut 和 @Cacheable缓存数据。

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
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
```

### 配置文件

resources/application.yml配置内容 ehcache.config指定ehcache的
```
spring:
  cache:
    type: ehcache
    ehcache:
      config: classpath:ehcache.xml

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

resources/ehcache.xml配置内容
``` 
<!-- ehcache配置 -->
<ehcache
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
        updateCheck="false">
    <!--缓存路径，用户目录下的ehcache目录-->
    <diskStore path="user.home/ehcache"/>

    <defaultCache
            maxElementsInMemory="20000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            overflowToDisk="true"
            maxElementsOnDisk="10000000"
            diskPersistent="false"
            diskExpiryThreadIntervalSeconds="120"
            diskSpoolBufferSizeMB="50"
            memoryStoreEvictionPolicy="LRU"/>

    <!--
    缓存文件名：student，同样的可以配置多个缓存
    maxElementsOnDisk：在磁盘上缓存的element的最大数目，默认值为0，表示不限制。
    eternal：设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断。
    overflowToDisk： 如果内存中数据超过内存限制，是否要缓存到磁盘上。
    diskPersistent：是否在磁盘上持久化。指重启jvm后，数据是否有效。默认为false。
    timeToIdleSeconds：对象空闲时间，指对象在多长时间没有被访问就会失效。只对eternal为false的有效。默认值0，表示一直可以访问。
    timeToLiveSeconds：对象存活时间，指对象从创建到失效所需要的时间。只对eternal为false的有效。默认值0，表示一直可以访问。
    diskSpoolBufferSizeMB：DiskStore使用的磁盘大小，默认值30MB。每个cache使用各自的DiskStore。
    diskExpiryThreadIntervalSeconds：对象检测线程运行时间间隔。标识对象状态的线程多长时间运行一次。
    -->
    <cache name="student"
           maxElementsInMemory="20000"
           eternal="true"
           overflowToDisk="true"
           diskPersistent="false"
           timeToIdleSeconds="0"
           timeToLiveSeconds="0"
           diskSpoolBufferSizeMB="50"
           diskExpiryThreadIntervalSeconds="120"/>

</ehcache>
```


### 代码解析

其他代码解析参考 spring-boot-demo-040-jpa 模块。

StudentServiceImpl.java 如下 @CachePut 和 @Cacheable都会将数据缓存到ehcache中
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

SpringBootDemo120EhcacheCacheApplication.java  要加上@EnableCaching
```
@SpringBootApplication
@EnableCaching
public class SpringBootDemo120EhcacheCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo120EhcacheCacheApplication.class, args);
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

可以查看到c:\users\funsonli\ehcache\student.data文件

浏览器再次访问 http://localhost:8080/student/view/381159203135426560
没有日志，表示从ehcache缓存中读取


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

