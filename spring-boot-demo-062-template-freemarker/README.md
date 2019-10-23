# Spring Boot入门样例-062-freemarker模板引擎

> 已经可以将数据库中数据读取出来，该在前端显示漂亮的界面。本demo演示如何使用freemarker模板引擎渲染出一个简单的登录界面。

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java和Maven安装配置(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
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
  freemarker:
    suffix: .ftl                                 # 设置模板后缀名
    content-type: text/html                      # 设置文档类型
    charset: UTF-8                               # 设置页面编码格式
    cache: false                                 # 设置页面缓存
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
public class User {
    private String name;
    private String password;
}
```

SiteController.java 如下
``` 
@Controller
public class SiteController {

    @GetMapping({"", "/", "index"})
    public String index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user  == null) {
            return "redirect:/login";
        }
        request.setAttribute("user", user);

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
        request.getSession().setAttribute("user", modelAttribute);
        return "redirect:/";
    }
}

```

1. index方法先判断是否登录，否则跳转login
2. get的login方法显示resources/templates/site/login.ftl文件，该文件会引入resources/templates/common/head.html，以及使用resources/static中的css和图片文件
3. 登录后显示resources/templates/site/index.ftl中内容

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/
浏览器访问 http://localhost:8080/login

```
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-060-template-01.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-060-template-03.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

