# Spring Boot入门样例-030-JDBC读取数据库中数据

> 数据存储在数据库中，需要展示给用户。本demo演示如何读取mysql数据库中数据，并展示给用户

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java安装(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
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
            <artifactId>spring-boot-starter-jdbc</artifactId>
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

application.yml配置内容
```
spring:
  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
    username: root
    password: root
```

### 代码解析
该项目有很多目录，分别说明如下：

- controller目录为控制器文件
- entity目录为实体目录，对应表格中的字段
- dao目录数据存取对象
- service为服务接口目录
- service/impl为服务接口具体实现目录
- util为工具类目录，加入分布式id雪花算法


Student.java 每个字段对应表格一个字段
``` 
@Data
@Component
public class Student {
    private static final long serialVersionUID = 1L;
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private Integer age;
}
```

StudentDao.java 操作表格的对象方法
``` 
@Repository
public class StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Student> list() {
        List<Student> list = jdbcTemplate.query("select * from student", new Object[]{}, new BeanPropertyRowMapper(Student.class));
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public int save(Student student) {
        return jdbcTemplate.update("insert into student(id, name, age) values(?, ?, ?)", student.getId(), student.getName(), student.getAge());
    }

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

        int res = studentService.save(model);
        return String.valueOf(res);
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/add/funson/30
浏览器访问 http://localhost:8080/student/add/zhonghua/28

浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson,funson, age=30), Student(id=aa, name=funson, age=2)]
```

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

