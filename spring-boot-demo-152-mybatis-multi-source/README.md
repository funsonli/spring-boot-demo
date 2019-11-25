# Spring Boot入门样例-151-JPA整合Jpa多数据源Multi Source

> 当系统拆分成多个，如用户表和订单表在不同的数据库。本demo演示如何使用mybatis整合多数据源Multi Source

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

数据表结构参考根目录下的db.sql
``` 
USE springbootdemo;
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

USE springbootdemo1;
DROP TABLE IF EXISTS `tbl_order`;
CREATE TABLE `tbl_order` (
  `id` varchar(255) NOT NULL,
  `student_id` varchar(255) NOT NULL,
  `sn` varchar(255) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

resources/application.yml配置内容
```
spring:
  datasource:
    user:
      driverClassName: com.mysql.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
      username: root
      password: root
    order:
      driverClassName: com.mysql.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306/springbootdemo1?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
      username: root
      password: root

mybatis:
  configuration:
    map-underscore-to-camel-case: true
```

### 代码解析
该项目有很多目录，分别说明如下：

- controller目录为控制器文件
- entity目录为实体目录，对应表格中的字段
- dao目录数据存取对象
- service为服务接口目录
- service/impl为服务接口具体实现目录
- util为工具类目录，加入分布式id雪花算法

以上解释可以参考[Spring Boot入门样例-040-JPA自动读取数据库中数据](https://github.com/funsonli/spring-boot-demo/blob/master/spring-boot-demo-040-jpa/README.md)

UserConfig.java 配置文件

``` 
@Configuration
@MapperScan(basePackages = "com.funsonli.springbootdemo152mybatismultisource.mapper.user", sqlSessionTemplateRef = "userSqlSessionTemplate")
public class UserConfig {

    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userSqlSessionFactory")
    @Primary
    public SqlSessionFactory userSqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/user/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "userTransactionManager")
    @Primary
    public DataSourceTransactionManager userTransactionManager(@Qualifier("userDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "userSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```

OrderConfig 订单表所在数据源配置
``` 
@Configuration
@MapperScan(basePackages = "com.funsonli.springbootdemo152mybatismultisource.mapper.order", sqlSessionTemplateRef = "orderSqlSessionTemplate")
public class OrderConfig {

    @Bean(name = "orderDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource orderDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "orderSqlSessionFactory")
    public SqlSessionFactory orderSqlSessionFactory(@Qualifier("orderDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/order/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "orderTransactionManager")
    public DataSourceTransactionManager orderTransactionManager(@Qualifier("orderDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "orderSqlSessionTemplate")
    public SqlSessionTemplate orderSqlSessionTemplate(@Qualifier("orderSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
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

    @Autowired
    OrderService orderService;

    @GetMapping({"", "/", "index"})
    public String index() {
        return studentService.index().toString() + " | " + studentService.index().toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        int res = studentService.save(model);
        return String.valueOf(res);
    }

    @GetMapping("/order/{studentId}/{amount}")
    public String order(HttpServletRequest request, @PathVariable String studentId, @PathVariable Integer amount) {
        Order model = new Order();
        model.setStudentId(studentId);
        model.setSn(UUID.randomUUID().toString().replace("-", ""));
        model.setAmount(amount);

        int i = orderService.save(model);
        return "ok";
    }
}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
往用户表中增加数据
浏览器访问 http://localhost:8080/student/add/funson/30
1
浏览器访问 http://localhost:8080/student/add/zhonghua/28
1

往订单表中增加数据
浏览器访问 http://localhost:8080/student/order/sssss/30
ok
浏览器访问 http://localhost:8080/student/order/ddddd/28
ok

浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson, age=30), Student(id=381159203135926560, name=zhonghua, age=28)), Order(id=396289506464108544, studentId=aa, sn=9f53a28efe50461e97700c0f13af414f, amount=123)]

可以在数据库中查看对应的数据是否生成
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

