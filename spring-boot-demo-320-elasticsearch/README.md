# Spring Boot入门样例-320-elasticsearch整合elasticsearch数据库

> elasticsearch是一款开源数据搜索和分析引擎。本demo演示如何演示如何整合elasticsearch存取数据。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### 准备

下载地址：https://www.elastic.co/downloads/past-releases

当前现在6.2.2版本 https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.2.2.tar.gz

启动：解压后在elasticsearch/bin目录中双击 

``` 
[2019-11-12T11:56:57,788][INFO ][o.e.t.TransportService   ] [BjlbBNk] publish_address {127.0.0.1:9300}, bound_addresses {127.0.0.1:9300}, {[::1]:9300}
[2019-11-12T11:57:00,890][INFO ][o.e.c.s.MasterService    ] [BjlbBNk] zen-disco-elected-as-master ([0] nodes joined), reason: new_master {BjlbBNk}{BjlbBNksQnCZ0bdMMqYdYw}{JwncO5VgTaOMzMhEY1GKSA}{127.0.0.1}{127.0.0.1:9300}
[2019-11-12T11:57:00,895][INFO ][o.e.c.s.ClusterApplierService] [BjlbBNk] new_master {BjlbBNk}{BjlbBNksQnCZ0bdMMqYdYw}{JwncO5VgTaOMzMhEY1GKSA}{127.0.0.1}{127.0.0.1:9300}, reason: apply cluster state (from master [master {BjlbBNk}{BjlbBNksQnCZ0bdMMqYdYw}{JwncO5VgTaOMzMhEY1GKSA}{127.0.0.1}{127.0.0.1:9300} committed version [1] source [zen-disco-elected-as-master ([0] nodes joined)]])
[2019-11-12T11:57:01,233][INFO ][o.e.g.GatewayService     ] [BjlbBNk] recovered [1] indices into cluster_state
[2019-11-12T11:57:01,506][INFO ][o.e.c.r.a.AllocationService] [BjlbBNk] Cluster health status changed from [RED] to [GREEN] (reason: [shards started [[name][0]] ...]).
[2019-11-12T11:57:01,590][INFO ][o.e.h.n.Netty4HttpServerTransport] [BjlbBNk] publish_address {127.0.0.1:9200}, bound_addresses {127.0.0.1:9200}, {[::1]:9200}
[2019-11-12T11:57:01,590][INFO ][o.e.n.Node               ] [BjlbBNk] started
```

可以访问 http://127.0.0.1:9200/ 获取elasticsearch相关信息
```
{
  "name" : "BjlbBNk",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "wkDP6LrTRnCTsUzBBabghA",
  "version" : {
    "number" : "6.2.2",
    "build_hash" : "10b1edd",
    "build_date" : "2018-02-16T19:01:30.685723Z",
    "build_snapshot" : false,
    "lucene_version" : "7.2.1",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}
```


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
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)
- Elasticsearch官方文档 https://www.elastic.co/guide/en/elasticsearch/reference/6.2/getting-started.html
- Spring boot Elastic文档 https://docs.spring.io/spring-data/elasticsearch/docs/3.2.1.RELEASE/reference/html/#reference


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

