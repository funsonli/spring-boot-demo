# Spring Boot入门样例-320-elasticsearch整合elasticsearch数据库

> elasticsearch是一款开源数据搜索和分析引擎。本demo演示如何演示如何整合elasticsearch存取数据。

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
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
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
  data:
    elasticsearch:
      cluster-nodes: localhost:9300
      properties:
        transport:
          tcp:
            connect_timeout: 120s
```

### 代码解析

StudentDao.java 通过@Document设置 索引库名  类型 分区数 备份
```
@Document(indexName = "name", type = "name", shards = 1, replicas = 0)
@Data
public class Student {
    private static final long serialVersionUID = 1L;
    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private Integer age;
}
```

StudentDao.java StudentDao继承ElasticsearchRepository<Student, String>
```
public interface StudentDao extends ElasticsearchRepository<Student, String> {
    public List<Student> findByName(String name);
    public List<Student> findByAge(Integer type);
    public List<Student> findByAgeBetween(Integer min, Integer max);

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

    @GetMapping("/view/{min}/{max}")
    public String view(HttpServletRequest request, @PathVariable Integer min, @PathVariable Integer max) {

        List<Student> models = studentDao.findByAgeBetween(min, max);
        return "ok " + models.toString();
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/add/funson/30
浏览器访问 http://localhost:8080/student/add/zhonghua/28
ok

浏览器访问 http://localhost:8080/student/view/funson
ok [Student(id=381579828207423488, name=funson, age=30)]


浏览器访问 http://localhost:8080/student/view/20/35  搜索区间值
ok [Student(id=381579828207423488, name=funson, age=30), Student(id=381580654049103872, name=zhonghua, age=28)]

浏览器访问 http://localhost:8080/student/view/30/35 搜索区间值
ok [Student(id=381579828207423488, name=funson, age=30)]

```

### 错误处理

```
NoNodeAvailableException: None of the configured nodes are available: [{#transport#-1}{xUV4oaoATNyIGBkhoy4bBA}{localhost}{127.0.0.1:9300}]
```
版本不对，请使用对应的版本，当前spring-boot-starter-data-elasticsearch对应的是6.2.x版本的elasticsearch，请下载6.2.2版本的elasticsearch


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- Elasticsearch官方文档 https://www.elastic.co/guide/en/elasticsearch/reference/6.2/getting-started.html
- Spring boot Elastic文档 https://docs.spring.io/spring-data/elasticsearch/docs/3.2.1.RELEASE/reference/html/#reference


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

