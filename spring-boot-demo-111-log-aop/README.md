# Spring Boot入门样例-111-log-aop面向切面日志

> 对于一些访问日志，写log.info太繁琐。本demo演示如何使用面向切面aop记录日志。

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
            <artifactId>spring-boot-starter-aop</artifactId>
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
无需配置
```

### 代码解析

BootanLog.java定义一个注解BootanLog，有value和type
``` 
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BootanLog {

    String value() default "";

    int type() default 1;
}
```

StudentController.java 如下 添加注解@BootanLog(value = "index", type = 2)
``` 
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @BootanLog(value = "index", type = 2)
    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @BootanLog(value = "view", type = 3)
    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        return "view " + id;
    }

}
```

BootanLogAspect.java 定义注解动作，我们可以在insertLog中将数据插入到数据库或者elasticsearch。
``` 
@Slf4j
@Aspect
@Component
public class BootanLogAspect {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(com.funsonli.springbootdemo111logaop.annotation.BootanLog)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();

        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();

            insertLog(point, endTime - beginTime);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();

        log.info(String.valueOf((int)(time)));

        BootanLog userAction = method.getAnnotation(BootanLog.class);
        if (userAction != null) {
            // 注解上的描述
            log.info(userAction.value());
            log.info(String.valueOf(userAction.type()));
        }

        Map<String, String[]> params = request.getParameterMap();
        log.info(request.getRequestURI());
        log.info(request.getMethod());

    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
浏览器访问 http://localhost:8080/student/view/funsonli

在控制台显示：
2019-10-14 15:44:10.773  INFO 16244 --- [nio-8080-exec-1] c.f.s.aop.BootanLogAspect                : 2
2019-10-14 15:44:10.774  INFO 16244 --- [nio-8080-exec-1] c.f.s.aop.BootanLogAspect                : index
2019-10-14 15:44:10.774  INFO 16244 --- [nio-8080-exec-1] c.f.s.aop.BootanLogAspect                : 2
2019-10-14 15:44:10.774  INFO 16244 --- [nio-8080-exec-1] c.f.s.aop.BootanLogAspect                : /student/
2019-10-14 15:44:10.774  INFO 16244 --- [nio-8080-exec-1] c.f.s.aop.BootanLogAspect                : GET

```

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

