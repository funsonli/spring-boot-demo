# Spring Boot 入门样例




## 项目简介

本项目作为Spring Boot入门样例，从浅到深用样例学习Spring Boot，并集成Spring Boot常用功能。

1. 入门级功能：包括Properties(配置)、JDBC Template(数据库CRUD)、JPA(Hibernate数据库CRUD)、Mybatis(数据库CRUD)、Mybatis(Mybatis数据库CRUD)、thymeleaf(模板解析)、beetl(模板解析)、freemarker(模板解析)、enjoy(模板解析)，并包含一些整合多个功能模块的090-blog(SSH实现一个简单的博客)
2. 常用功能：session(会话)、security(rbac权限框架)、shiro(rbac权限框架)、oauth(第三方登录)、logback(日志)、log-aop(AOP方式日志)、ehcache(ehcache缓存)、exception(统一异常处理)、Async(异步)、multi-source(多数据源)、mycat(分库分表)、task(定时任务)、quartz(定时任务)、xxl-job(定时任务)、i18n(国际化)、websocket、sso(统一登录入口)
3. 第三方常用功能：actuator(监控)、admin(监控)、swagger(接口文档)、swagger-bootstrap-ui(结构文档美化)、guava(缓存和限流)、social(第三方登录)、zookeeper(分布式锁)、docker(容器部署)
4. 整合Nosql：redis(redis连接、redis缓存、redis哨兵模式)、mongodb(Mongodb)、elasticsearch、memcache、neo4j、hadoop、ldap
5. 整合消息队列Message Queue：rabbitmq、kafka、activemq、rocketmq
6. 辅助功能：upload-qiniu(七牛云上传)、upload-aliyun-oss(阿里云OSS上传)、uploader(工厂模式图片上传)
7. 第三方复杂功功能：activiti(工作流引擎)
8. 分布式相关功能：dubbo(RPC)、jta-atomikos(分布式事务)、spring-cloud(微服务)
9. Bootan项目解析：Bootan(Admin带RBAC的角色的快速开发启动器)
10. 专题功能：秒杀(如何实现高并发秒杀)

> 项目开发计划参考 [pom.xml](https://github.com/funsonli/spring-boot-demo/blob/master/pom.xml) 中的modules部分，未注释的为已经完成，注释的为已经待开发。

各模块中间的三位数字的解释如下：

```
1. 第1位为分类
- 0xx系列为入门功能
- 1xx系列为Spring Boot常用功能
- 2xx系列和Spring Boot相关的第三方项目
- 3xx为Spring Boot集成Nosql数据库
- 4xx为Spring Boot集成消息队列
- 5xx为Spring Boot频率不大高的功能
- 7xx-9xx为一些相对复杂的功能

- bxx为如何一步步开Bootan后台管理系统样例

2. 第2位为功能

3. 第三位3为扩展，0为该功能的基础，1以后为该功能的扩展，可以忽略

```

### 如何运行

1. 准备Java和maven环境 [Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
2. 配置IDEA和安装相关插件 [idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
3. 在IDEA中运行对应的样例 [如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> 如果在idea中运行慢，建议将mvn的源设置为国内的，参考[配置mirror为国内源](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md#mirror)

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 

### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan快速开发后台 https://github.com/funsonli/bootan
- Bootan前端 https://github.com/funsonli/bootan-frontend


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

