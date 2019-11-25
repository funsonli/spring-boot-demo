# Spring Boot入门样例-151-JPA-multi-source整合Jpa多数据源Multi Source

> 当系统拆分成多个，如用户表和订单表在不同的数据库。本demo演示如何使用jpa hibernate整合Jpa多数据源Multi Source

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
      url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
      username: root
      password: root
    order:
      driverClassName: com.mysql.jdbc.Driver
      url: jdbc:mysql://localhost:3306/springbootdemo1?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
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

以上解释可以参考[Spring Boot入门样例-040-JPA自动读取数据库中数据](https://github.com/funsonli/spring-boot-demo/blob/master/spring-boot-demo-040-jpa/README.md)

DataSourceConfig.java 配置文件解析

``` 
@Configuration
public class DataSourceConfig {

    /**
     * user库
     * @author Funsonli
     * @date 2019/11/25
     */
    @Primary
    @Bean(name = "userDataSourceProperties")
    @Qualifier("userDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "userDataSource")
    @Qualifier("userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * order库
     * @author Funsonli
     * @date 2019/11/25
     */
    @Bean(name = "orderDataSourceProperties")
    @Qualifier("orderDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSourceProperties orderDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "orderDataSource")
    @Qualifier("orderDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource orderDataSource(@Qualifier("orderDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

}
```

UserConfig 用户表所在数据源配置
``` 
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager",
        basePackages = {"com.funsonli.springbootdemo151jpamultisource.dao.user"})

public class UserConfig {
    @Autowired
    private HibernateProperties hibernateProperties;
    @Resource
    @Qualifier("userDataSource")
    private DataSource userDataSource;

    @Primary
    @Bean(name = "userEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return userEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Resource
    private JpaProperties jpaProperties;

    /**
     * 设置实体类所在位置
     */
    @Primary
    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder
                .dataSource(userDataSource)
                .packages("com.funsonli.springbootdemo151jpamultisource.entity.user")
                .persistenceUnit("userPersistenceUnit")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager userTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(userEntityManagerFactory(builder).getObject());
    }
}
```

OrderConfig 订单表所在数据源配置
``` 
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager",
        basePackages = {"com.funsonli.springbootdemo151jpamultisource.dao.order"})
public class OrderConfig {

    @Autowired
    @Qualifier("orderDataSource")
    private DataSource orderDataSource;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Bean(name = "orderEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return orderEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Resource
    private JpaProperties jpaProperties;

    @Bean(name = "orderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder
                .dataSource(orderDataSource)
                .packages("com.funsonli.springbootdemo151jpamultisource.entity.order")
                .persistenceUnit("orderPersistenceUnit")
                .properties(properties)
                .build();
    }

    @Bean(name = "orderTransactionManager")
    PlatformTransactionManager orderTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(orderEntityManagerFactory(builder).getObject());
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
        return studentService.index().toString() + " | " + orderService.index().toString();
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        Student model = new Student();
        model.setName(name);
        model.setAge(age);

        model = studentService.save(model);
        return model.toString();
    }

    @GetMapping("/order/{studentId}/{amount}")
    public String order(HttpServletRequest request, @PathVariable String studentId, @PathVariable Integer amount) {
        Order model = new Order();
        model.setStudentId(studentId);
        model.setSn(UUID.randomUUID().toString().replace("-", ""));
        model.setAmount(amount);

        model = orderService.save(model);
        return model.toString();
    }
}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
往用户表中增加数据
浏览器访问 http://localhost:8080/student/add/funson/30
浏览器访问 http://localhost:8080/student/add/zhonghua/28

往订单表中增加数据
浏览器访问 http://localhost:8080/student/order/sssss/30
浏览器访问 http://localhost:8080/student/order/ddddd/28

浏览器访问 http://localhost:8080/student/
[Student(id=381159203135426560, name=funson, age=30), Student(id=381159203135926560, name=zhonghua, age=28)), Order(id=396289506464108544, studentId=aa, sn=9f53a28efe50461e97700c0f13af414f, amount=123)]

可以在数据库中查看对应的数据是否生成
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

