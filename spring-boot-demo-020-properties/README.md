# Spring Boot入门样例-020-properties配置

> 网站总是要给前端展示信息，先把配置文件中的数据展示到前端。本demo演示如何读取Spring Boot系统配置，并支持开发环境和正式环境配置切换

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java安装(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
其中spring-boot-starter-web和lombok是必须的，具体参见该项目的pox.xml
```
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
默认为application.properties，但是因为配置多会比较臃肿，所以我们后续采用yml方式，只需要将该文件修改为application.yml即可

active: prod 表示使用application-prod.yml中的配置, 其中student读取application.yml

application.yml配置内容，
```
demo:
  student:
    name: funson
    age: 30

spring:
  profiles:
    active: prod # 表示使用application-prod.yml中的配置
```
application-prod.yml配置内容
```
 demo:
   book:
     name: spring boot demo prod
     price: 59
     authors:
       - funson
       - fuson1
```
application-dev.yml配置内容
```
 demo:
   book:
     name: spring boot demo prod
     price: 69
     authors: # 配置项支持数组
       - funson
       - fuson1
```

BookProperties.java解析配置项

```
@Data
@Component
@ConfigurationProperties(prefix = "demo.book")
public class BookProperties {
    private String name;
    private Integer price;
    private List<String> authors;
}
```
- @Data 为lombok插件，让我们省略很多get set代码
- @ConfigurationProperties 表示为配置项，prefix表示前缀
- @Componet表示该配置为bean，在controller里面可以使用@Autowired自动生成

BookController.java
``` 
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookProperties bookProperties;

    @GetMapping({"/", "index"})
    public String index() {
        return "book name: " + bookProperties.getName() + " and price: " + bookProperties.getPrice() + " and authors: " + bookProperties.getAuthors().toString();
    }
}
```


### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
student name: funson and age: 30

浏览器访问 http://localhost:8080/book/index
book name: spring boot demo prod and price: 59 and authors: [funson, fuson1]
```

输出price:59表示使用了application-prod.yml中的配置

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

