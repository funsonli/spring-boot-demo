# Spring Boot入门样例-130-excetion统一全局异常处理

> 程序运行期间可能会有各种错误，每个地方加try catch比较麻烦。本demo演示如何在一个地方统一处理异常，减少开发代码。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> Spring Boot 提供@RestControllerAdvice 和 @ExceptionHandler 两个注解完成全局统一异常处理

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容 ehcache.config指定ehcache的
```
无
```


### 代码解析

BootanException.java 如下 自定义异常
``` 
/**
 * 自定义异常
 *
 * @author Funsonli
 * @date 2019/11/19
 */
@Data
public class BootanException extends RuntimeException {

    private String message;

    public BootanException(String message) {
        super(message);
        this.message = message;
    }

}


```

RestExceptionHandler.java 如下 统一返回Rest方式的数据
``` 
/**
 * 异常处理类
 *
 * @author Funsonli
 * @date 2019/11/19
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BootanException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResult handleBootanException(BootanException e) {

        if (e != null) {
            log.info(e.toString(), e);
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.error();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResult handleException(BootanException e) {

        if (e != null) {
            log.info(e.toString(), e);
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.error();
    }

}

```


StudentController.java 如下 分别使用自定义异常和系统异常
``` 
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping({"", "/", "index"})
    public String index() {
        throw new BootanException("index error");
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id) throws Exception {

        int i = 3 / id;
        return id + " id divide by 3 ";
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/student/
{"code":500,"data":null,"message":"index error","status":500,"timestamp":1574134955395}


浏览器访问 http://localhost:8080/student/view/1
1 id divide by 3

Postman访问 http://localhost:8085/student/view/0
{
    "timestamp": "2019-11-19T03:39:22.284+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "/ by zero",
    "path": "/student/view/0"
}
```


### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

