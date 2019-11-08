# Spring Boot入门样例-161-quartz整合quartz定时任务

> 使用官方提供的quartz增强定时任务功能，可以把定时任务保存在数据库中，通过网页进行访问。本demo演示如何通过字符串和网页来管理定时任务。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> 后台网页管理定时任务请参考Bootan中的定时任务管理

### 数据表

请参考根目录下的 `quartz.sql`

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

### 配置文件

resources/application.yml配置内容 
```
spring:
  datasource:
    url: jdbc:mysql://locahost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
    username: root
    password: root

  quartz:
    # 参见 org.springframework.boot.autoconfigure.quartz.QuartzProperties
    job-store-type: jdbc
    wait-for-jobs-to-complete-on-shutdown: true
    properties:
      org:
        quartz:
          threadPool:
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
          jobStore:
            misfireThreshold: 5000
```

### 代码解析


SampleJob.java 如下 一个带参数的job
``` 
@Data
@Slf4j
public class SampleJob implements Job {

    private String parameter;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("欢迎使用Spring Boot Demo by funsonli " + parameter);
    }
}
```

StudentController.java 通过网页管理定时任务增加/暂停/恢复执行/删除
```
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    // 此处为字符串，也可以从数据表中读取出来
    private static final String jobName = "com.funsonli.springbootdemo161quartz.job.SampleJob";
    private static final String cronExpression = "0/3 * * * * ?";
    private static final String parameter = "param";

    @Autowired
    private Scheduler scheduler;

    @GetMapping({"", "/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping({"add"})
    public String add() {
        addJob(jobName);
        return "add job";
    }

    @GetMapping({"pause"})
    public String pause() {
        pauseJob(jobName);
        return "pause job";
    }

    @GetMapping({"resume"})
    public String resume() {
        resumeJob(jobName);
        return "resume job";
    }

    @GetMapping({"delete"})
    public String delete() {
        deleteJob(jobName);
        return "delete job";
    }

    private void addJob(String jobClassName) {
        try {
            // 启动调度器
            scheduler.start();

            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName)
                    .usingJobData("parameter", parameter)
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

    private void pauseJob(String jobClassName) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobClassName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void resumeJob(String jobClassName) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobClassName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(String jobClassName){

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Job getClass(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        return (Job)clazz.newInstance();
    }

}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器分别访问：http://localhost:8080/student/add
浏览器分别访问：http://localhost:8080/student/pause
浏览器分别访问：http://localhost:8080/student/resume
浏览器分别访问：http://localhost:8080/student/delete

控制台日志: 
2019-11-08 15:25:30.001  INFO 9620 --- [eduler_Worker-2] c.f.s.job.SampleJob                      : 欢迎使用Spring Boot Demo by funsonli param
2019-11-08 15:25:33.000  INFO 9620 --- [eduler_Worker-3] c.f.s.job.SampleJob                      : 欢迎使用Spring Boot Demo by funsonli param
2019-11-08 15:25:36.001  INFO 9620 --- [eduler_Worker-4] c.f.s.job.SampleJob                      : 欢迎使用Spring Boot Demo by funsonli param
2019-11-08 15:25:48.000  INFO 9620 --- [eduler_Worker-5] c.f.s.job.SampleJob                      : 欢迎使用Spring Boot Demo by funsonli param

前面两条日志为增加时打印出来，后面两条为resume恢复执行时打印出来
```


### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- Quartz官方文档 http://www.quartz-scheduler.org/documentation/quartz-2.2.x/quick-start.html


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

