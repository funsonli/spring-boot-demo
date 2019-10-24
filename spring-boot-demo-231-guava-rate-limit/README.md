# Spring Boot入门样例-231-guava-rate-limit整合guava限流

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
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>25.1-jre</version>
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
/**
 * 限流注解
 * @author funsonli
 */
//作用于方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    // 限制次数
    double limit() default 5;

    //超时时长
    int timeout() default 1000;

    //超时时间单位
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
```

StudentController.java 如下 添加注解@RateLimiter(limit = 1, timeout = 500)
``` 
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @RateLimiter(limit = 1, timeout = 500)
    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @RateLimiter(limit = 3, timeout = 1000)
    @GetMapping("/view/{id}")
    public String view(@PathVariable String id) {
        return "view " + id;
    }

}
```

RateLimiterAspect.java 定义注解动作，使用guava提供的RateLimiter。
``` 
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    private static final ConcurrentMap<String, com.google.common.util.concurrent.RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.funsonli.springbootdemo231guavaratelimit.annotation.RateLimiter)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //获取注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null && rateLimiter.limit() > 0) {
            double limit = rateLimiter.limit();
            if (rateLimiterCache.get(method.getName()) == null) {
                rateLimiterCache.put(method.getName(), com.google.common.util.concurrent.RateLimiter.create(limit));
            }

            log.info("限流设置为: " + rateLimiterCache.get(method.getName()).getRate());
            // 尝试获取令牌
            if (rateLimiterCache.get(method.getName()) != null && !rateLimiterCache.get(method.getName()).tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                log.info("点太快了，等会儿~");
                throw new RuntimeException("点太快了，等会儿~");
            }
        }
        return point.proceed();
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/ 
浏览器访问 http://localhost:8080/student/view/1

快速刷新网页
在控制台显示：
2019-10-24 12:15:42.597  INFO 7556 --- [nio-8080-exec-5] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:42.765  INFO 7556 --- [nio-8080-exec-6] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:42.980  INFO 7556 --- [nio-8080-exec-7] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:42.980  INFO 7556 --- [nio-8080-exec-7] c.f.s.aop.RateLimiterAspect              : 点太快了，等会儿~
2019-10-24 12:15:43.199  INFO 7556 --- [nio-8080-exec-8] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:43.323  INFO 7556 --- [nio-8080-exec-9] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:43.324  INFO 7556 --- [nio-8080-exec-9] c.f.s.aop.RateLimiterAspect              : 点太快了，等会儿~
2019-10-24 12:15:43.473  INFO 7556 --- [io-8080-exec-10] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0
2019-10-24 12:15:43.621  INFO 7556 --- [nio-8080-exec-1] c.f.s.aop.RateLimiterAspect              : 限流设置为: 3.0

```

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

