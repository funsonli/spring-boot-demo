# Spring Boot入门样例-310-mongodb整合mongodb数据库

> Mongodb是一款高性能内存Nosql数据库，支持对键值的查询。本demo演示如何演示如何mongodb缓存数据并读取。

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
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
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
spring:
  # mongodb
  data:
    mongodb:
      uri: mongodb://127.0.0.1:27017/spring-boot-demo-db

```

### 代码解析

StudentDao.java StudentDao继承MongoRepository<Student, String>
```
@Component
public interface StudentDao extends MongoRepository<Student, String> {
    public List<Student> findByName(String name);
    public List<Student> findByAge(Integer type);

}
```

StudentController.java 如下 @CachePut 和 @Cacheable都会将数据缓存到Redis中
``` 
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentDao studentDao;

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {

        List<Student> models = studentDao.findByName(id);
        return "ok " + models.toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        studentDao.save(model);
        return "ok";
    }
}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/add/funson/30
ok

浏览器访问 http://localhost:8080/student/view/funson
 [Student(id=381213910235222016, name=funson, age=30)]
```


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

