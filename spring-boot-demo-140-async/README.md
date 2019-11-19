# Spring Boot入门样例-140-async异步执行async

> 比如用户注册之后，要给用户发邮件，万一邮件服务坏了，用户可能要等半天，我们可以使用异步方法先给用户返回，其他线程需要执行发邮件的动作。本demo演示如何使用async线程池使用其他线程去执行非主线任务。

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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容 
```
无
```



### 代码解析

SpringBootDemo140AsyncApplication.java 如下 增加@EnableAsync注解
``` 
@SpringBootApplication
@EnableAsync
public class SpringBootDemo140AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo140AsyncApplication.class, args);
    }

}
```

MyAsyncConfigurer.java  异步线程池
```
@Slf4j
@Component
public class MyAsyncConfigurer implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        //定义一个核心为10个线程数量的线程池
        return new ThreadPoolExecutor(10, 20, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.info("Exception message - " + throwable.getMessage());
            log.info("Method name - " + method.getName());
            for (Object param : objects) {
                log.info("Parameter value - " + param);
            }
        }
    }

}
```

StudentService.java  定义3个异步方法，分别打印当前的线程名
```
@Slf4j
@Service
public class StudentService {

    /**
     *  无参数 无返回值
     * @author Funsonli
     * @date 2019/11/7
     */
    @Async
    public void async1() {
        try {
            Thread.sleep(5 * 1000);
            log.info("async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  有参数 无返回值
     * @author Funsonli
     * @date 2019/11/7
     * @param s :
     * @return : null
     */
    @Async
    public void async2(String s) {
        try {
            Thread.sleep(5 * 1000);
            log.info(s + " async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  有参数 有返回值
     * @author Funsonli
     * @date 2019/11/7
     * @param s :
     * @return : Future
     */
    @Async
    public Future async3(String s) {
        Future future;
        try {
            Thread.sleep(5 * 1000);
            log.info(s + "async funson " + Thread.currentThread().getName());
            future = new AsyncResult(s + "async funson " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            future = new AsyncResult(s + " error " + Thread.currentThread().getName());
            e.printStackTrace();
        }
        return future;
    }

}
```

StudentController.java
``` 
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping({"", "/", "index"})
    public String index() {
        studentService.async1();
        studentService.async2("2 ");
        Future future = studentService.async3("3 ");
        log.info("do first " + Thread.currentThread().getName());
        try {
            log.info(future.get().toString() + " back");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index funson " + Thread.currentThread().getName();
    }

}
```


### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
index funson http-nio-8080-exec-3


控制台日志: 
2019-11-07 16:52:20.149  INFO 7596 --- [nio-8089-exec-4] c.f.s.controller.StudentController       : do first http-nio-8089-exec-4
2019-11-07 16:52:25.150  INFO 7596 --- [pool-1-thread-4] c.f.s.service.StudentService             : async funson pool-1-thread-4
2019-11-07 16:52:25.150  INFO 7596 --- [pool-1-thread-5] c.f.s.service.StudentService             : 2  async funson pool-1-thread-5
2019-11-07 16:52:25.150  INFO 7596 --- [pool-1-thread-6] c.f.s.service.StudentService             : 3 async funson pool-1-thread-6
2019-11-07 16:52:25.150  INFO 7596 --- [nio-8089-exec-4] c.f.s.controller.StudentController       : 3 async funson pool-1-thread-6back

每个异步方法使用不同的线程去执行，主线程先输出 do first日志
```


### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

