# Spring Boot入门样例-090-blog使用JPA和thymeleaf实现一个简单的博客

> 前面的功能组织起来是SSH或SSM项目了。本demo演示如何使用JPA和thymeleaf模板引擎实现一个简单的博客，包含登录、发布博客、博客列表、查看具体博客内容。

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
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
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

  thymeleaf:
    cache: false
    servlet:
      content-type: text/html
    mode: HTML
    encoding: UTF-8
```

### 数据库文件

数据库文件请查看根目录的db.sql文件，本demo只列出一些最简单的字段。
``` 
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `user` VALUES ('aasfsfsnolfjs', 'admin', '123456');

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `content` text,
  `click` int(11) DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `post` VALUES ('386158923117367296', 'haha', 'good', '0', '2019-10-28 11:38:47', '2019-10-28 11:38:47');
```

### 代码解析
该项目有很多目录，分别说明如下：

- controller目录为控制器文件
- entity目录为实体目录，对应表格中的字段（本样例不查询数据库）
- resources/templates对应模板文件路径
- resources/static静态文件路径，包括css和images图片


User.java 每个字段对应表格一个字段（本样例不查询数据库，先不对应）
``` 
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String username;
    private String password;
}
```

Post.java 每个字段对应表格一个字段（本样例不查询数据库，先不对应）
``` 
@Data
@Entity
@Table(name = "post")
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());
    private String name;
    private String content;
    private Integer click = 0;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
```

SiteController.java 如下
``` 
@Controller
public class SiteController {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @GetMapping({"", "/", "index"})
    public String index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("user", user);
        }

        List<Post> posts = postService.index();
        request.setAttribute("posts", posts);

        return "site/index";
    }

    @GetMapping("/login")
    public String login() {
        return "site/login";
    }

    @PostMapping("/login")
    public String save(@ModelAttribute User modelAttribute, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "binding error";
        }

        User model = userService.findByUsername(modelAttribute.getUsername());
        if (model != null && model.getPassword().equals(modelAttribute.getPassword())) {
            request.getSession().setAttribute("user", modelAttribute);
        }

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("user", user);
        }

        Post post = postService.findById(id);
        request.setAttribute("post", post);

        return "site/post";
    }

    @GetMapping("/post/add")
    public String postAdd(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        } else {
            request.setAttribute("user", user);
        }

        return "site/post-add";
    }

    @PostMapping("/post/add")
    public String postSave(@ModelAttribute Post modelAttribute, BindingResult result, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        postService.save(modelAttribute);

        return "redirect:/";
    }

}

```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/
浏览器访问 http://localhost:8080/login

```
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-090-01.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-090-03.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

