# Spring Boot入门样例-040-JPA自动读取数据库中数据

> JDBC方式写sql语句读取数据库中数据，每个表都要写而且大同小异。本demo演示如何使用jpa hibernate自动读取数据库中数据

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
该项目有很多目录，分别说明如下：

- controller目录为控制器文件
- entity目录为实体目录，对应表格中的字段
- dao目录数据存取对象
- service为服务接口目录
- service/impl为服务接口具体实现目录
- util为工具类目录，加入分布式id雪花算法


Student.java 每个字段对应表格一个字段，需要指定@Entity和表格@Table和@Id
``` 
@Data
@Entity
@Table(name = "student")
public class Student {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    private String name;

    private Integer age;
}
```

StudentDao.java 操作表格的对象方法，不用写一行sql定义了
``` 
@Repository
public interface StudentDao extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {

}
```

StudentService.java 提供给controller的服务接口
``` 
public interface StudentService {
    List<Student> index();
    Integer save(Student student);
}
```

StudentServiceImpl.java 服务接口的具体实现，使用Dao操作数据库
``` 
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> index() {
        return studentDao.list();
    }

    @Override
    public Integer save(Student student) {
        return studentDao.save(student);
    }
}
```

StudentController.java 控制器，调用service操作数据库
``` 
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping({"", "/", "index"})
    public String index() {
        return studentService.index().toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        model = studentService.save(model);
        return model.toString();
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/add/funson/30
浏览器访问 http://localhost:8080/student/add/zhonghua/28

浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson, age=30), Student(id=381159203135926560, name=zhonghua, age=26)]
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

