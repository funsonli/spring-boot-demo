# Spring Boot入门样例-160-task定时任务

> 比如游戏一般在凌晨3点给用户结算当天的签到，再签到就算第二天的任务，需要使用定时任务。本demo演示如何在程序中实现定时任务。

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
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>4.6.6</version>
            <scope>compile</scope>
        </dependency>
```

### 配置文件

resources/application.yml配置内容 
```
无
```

### 代码解析


SpringBootDemo160TaskApplication.java 如下 增加@EnableAsync注解
``` 
@EnableScheduling
@SpringBootApplication
public class SpringBootDemo160TaskApplication {

    public static void main(String[] args) {
        System.out.println("main thread " + Thread.currentThread().getName());
        SpringApplication.run(SpringBootDemo160TaskApplication.class, args);
    }

}
```

TaskConfig.java  任务线程池
```
@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    /**
     * 设置定时任务线程池，设置10个名称为 task-开头的线程
     * @author Funsonli
     * @date 2019/11/8
     * @return : Executor
     */
    @Bean
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(10, new ThreadFactoryBuilder().setNamePrefix("task-").build());
    }
}

```

SampleTask.java  定义3种定时任务方法
```
@Slf4j
@Component
public class SampleTask {
    /**
     * 和linux cron类似，每5秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        log.info("【task1】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

    /**
     * 每10秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(fixedRate = 10000)
    public void task2() {
        log.info("【task2】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

    /**
     * 延迟5秒，每隔6秒执行一次
     * @author Funsonli
     * @date 2019/11/8
     */
    @Scheduled(fixedDelay = 6000, initialDelay = 5000)
    public void task3() {
        log.info("【task3】：{}, Thread: {}", new Date().toString(), Thread.currentThread().getName());
    }

}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
控制台日志: 
main thread main
xxx
2019-11-08 12:10:25.003  INFO 6164 --- [         task-1] c.f.s.task.SampleTask                    : 【task1】：Fri Nov 08 12:10:25 CST 2019, Thread: task-1
2019-11-08 12:10:27.111  INFO 6164 --- [         task-8] c.f.s.task.SampleTask                    : 【task3】：Fri Nov 08 12:10:27 CST 2019, Thread: task-8
2019-11-08 12:10:27.994  INFO 6164 --- [         task-2] c.f.s.task.SampleTask                    : 【task2】：Fri Nov 08 12:10:27 CST 2019, Thread: task-2
2019-11-08 12:10:30.001  INFO 6164 --- [         task-9] c.f.s.task.SampleTask                    : 【task1】：Fri Nov 08 12:10:30 CST 2019, Thread: task-9
2019-11-08 12:10:33.112  INFO 6164 --- [         task-4] c.f.s.task.SampleTask                    : 【task3】：Fri Nov 08 12:10:33 CST 2019, Thread: task-4
2019-11-08 12:10:35.002  INFO 6164 --- [         task-3] c.f.s.task.SampleTask                    : 【task1】：Fri Nov 08 12:10:35 CST 2019, Thread: task-3
2019-11-08 12:10:37.993  INFO 6164 --- [         task-0] c.f.s.task.SampleTask                    : 【task2】：Fri Nov 08 12:10:37 CST 2019, Thread: task-0
2019-11-08 12:10:39.113  INFO 6164 --- [         task-5] c.f.s.task.SampleTask                    : 【task3】：Fri Nov 08 12:10:39 CST 2019, Thread: task-5

所有的定时任务使用task-开头的线程池， 主线程名城为main，和定时任务线程不一样
```


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

