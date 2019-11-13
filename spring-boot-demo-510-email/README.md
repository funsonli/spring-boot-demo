# Spring Boot入门样例-510-email使用Java Mail发送邮件

> 给用户发送邮件是一项基本功能。本demo演示如何使用Java Mail发送邮件。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> hadoop 安装请参考 http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.ulisesbocchio</groupId>
            <artifactId>jasypt-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>
        <dependency>
            <groupId >com.sun.mail</groupId >
            <artifactId >javax.mail</artifactId >
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>28.1-jre</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>5.2.1.RELEASE</version>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
spring:
  mail:
    host: smtp.163.com
    port: 465
    username: funson@163.com
    password: ENC(wqg+4BsSsdfsf62keoiEpEZllpcJG)
    protocol: smtp
    test-connection: true
    default-encoding: UTF-8
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
      mail.smtp.starttls.required: true
      mail.smtp.ssl.enable: true
      mail.display.sendmail: spring-boot-demo

jasypt:
  encryptor:
    password: funson
```

### 代码解析

EmailUtil.java 如下 
``` 
@Slf4j
@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from; //读取配置文件中的参数

    @Async
    public void sendEmailText(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Async
    public void sendEmailHtml(String to, String subject, String templateName, Object o) {
        Context context = new Context();
        context.setVariables(beanToMap(o));
        String content = templateEngine.process(templateName, context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true,"utf-8");
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }


    public <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

}
```

Message.java 如下 封装上传下载文件
```
@Data
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String message;
}

```

Message.java 如下 封装上传下载文件
```
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    EmailUtil emailUtil;

    @GetMapping({"", "/", "index"})
    public String index() {
        emailUtil.sendEmailText("funsonli@qq.com", "content", "content");
        return "send funson success";
    }

    @GetMapping("/html")
    public String html() {
        Message message = new Message(5, "funson");

        emailUtil.sendEmailHtml("funsonli@qq.com", "hello", "register-ok", message);
        return "send funson html success";
    }

}
```

register-ok.html 如下 邮件模板
``` 
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
</head>
<body>
<h1>welcome</h1>

注册成功 <span style="color:blue" th:text="${message}"></span>
</body>
</html>
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/index 发送普通邮件 

浏览器访问 http://localhost:8080/student/html 发送html邮件 

```

### 错误解决

1.没有连接
控制台报错
```
2019-11-13 15:06:56.018 ERROR 225568 --- [nio-8081-exec-1] c.f.s.util.EmailUtil                     : org.springframework.mail.MailSendException: Failed messages: com.sun.mail.smtp.SMTPSendFailedException: 554 DT:SPM 163 smtp14,EsCowABHTDWOq8tdrd74HA--.185S2 1573628815,please see http://mail.163.com/help/help_spam_16.htm?ip=61.141.75.133&hostid=smtp14&time=1573628815
; message exceptions (1) are:
Failed message 1: com.sun.mail.smtp.SMTPSendFailedException: 554 DT:SPM 163 smtp14,EsCowABHTDWOq8tdrd74HA--.185S2 1573628815,please see http://mail.163.com/help/help_spam_16.htm?ip=61.141.75.133&hostid=smtp14&time=1573628815
```
html在163管理的比较严，163认为是垃圾邮件，请使用其它邮箱，或者多发几次，或者修改register-ok.html文件


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

