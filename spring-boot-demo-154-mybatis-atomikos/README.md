# Spring Boot入门样例-154-mybatis-atomikos整合Atomikos和Mybatis处理多数据库分布式事务

> 比如用户下单成功，必须扣减用户的余额，此时两个库的数据需要同时成功或者同时失败。本demo演示如何使用mybatis整合Atomikos进行多数据库分布式事务操作

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
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jta-atomikos</artifactId>
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

以上解释可以参考[Spring Boot入门样例-152-mybatis-multi-source整合Jpa多数据源Multi Source](https://github.com/funsonli/spring-boot-demo/blob/master/spring-boot-demo-152-mybatis-multi-source/README.md)

UserConfig 用户表所在数据源配置
``` 
@Configuration
@MapperScan(basePackages = "com.funsonli.springbootdemo154mybatisatomikos.mapper.user", sqlSessionTemplateRef = "userSqlSessionTemplate")
public class UserConfig {

    /**
     * 注解@Primary只能有一个地方有 setUniqueResourceName名字需唯一
     * @author Funsonli
     * @date 2019/11/26
     */
    @Bean(name = "userDataSource")
    @Primary
    public DataSource testDataSource(UserProperties userProperties) throws Exception {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(userProperties.getJdbcUrl());
        mysqlXADataSource.setUser(userProperties.getUsername());
        mysqlXADataSource.setPassword(userProperties.getPassword());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("userDataSource");

        return atomikosDataSourceBean;
    }

    /**
     * 设置mapper的xml文件路径
     * @author Funsonli
     * @date 2019/11/26
     */
    @Bean(name = "userSqlSessionFactory")
    @Primary
    public SqlSessionFactory userSqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/user/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        bean.setConfiguration(configuration);
        return bean.getObject();
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
@MapperScan(basePackages = "com.funsonli.springbootdemo154mybatisatomikos.mapper.order", sqlSessionTemplateRef = "orderSqlSessionTemplate")
public class OrderConfig {

    @Bean(name = "orderDataSource")
    public DataSource testDataSource(OrderProperties orderProperties) throws Exception {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(orderProperties.getJdbcUrl());
        mysqlXADataSource.setUser(orderProperties.getUsername());
        mysqlXADataSource.setPassword(orderProperties.getPassword());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("orderDataSource");

        return atomikosDataSourceBean;
    }

    @Bean(name = "orderSqlSessionFactory")
    @Primary
    public SqlSessionFactory orderSqlSessionFactory(@Qualifier("orderDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/order/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        bean.setConfiguration(configuration);
        return bean.getObject();
    }

    @Bean(name = "orderSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate orderSqlSessionTemplate(@Qualifier("orderSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

AddServiceImpl.java 服务层，方法需要加上@Transactional注解
``` 
@Service
public class AddServiceImpl implements AddService {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    @Transactional
    public void add(String name) {
        Student student = new Student();
        String studentId = String.valueOf(SnowFlake.getInstance().nextId());
        student.setId(studentId);
        student.setName(name);
        student.setAge(18);
        studentMapper.save(student);

        if ("0".equals(name)) {
            int k = 10 / Integer.valueOf(name);
        }

        Order order = new Order();
        order.setStudentId(studentId);
        order.setId(String.valueOf(SnowFlake.getInstance().nextId()));
        order.setSn(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(0);
        orderMapper.save(order);
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
    AddService addService;

    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping("/add/{name}/{age}")
    public String add(HttpServletRequest request, @PathVariable String name, @PathVariable Integer age) {
        addService.add(name);
        return "ok";
    }
}


```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/add/1/30
两边的表都可以生成数据

浏览器访问 http://localhost:8080/student/add/0/30
数据表user中没有数据，表示遇到异常，会进行跨数据库回滚
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

