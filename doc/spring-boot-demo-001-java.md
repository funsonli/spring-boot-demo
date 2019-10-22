# Spring Boot入门样例-001-Java安装

> 本文说明Java在windows下的安装和配置

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java安装(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)
- Spring Boot入门样例-007-pom.xml说明(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-007-pom.md)


### 1. 下载
下载地址：https://www.oracle.com/technetwork/java/javase/downloads/index.html

目前最新的为Java SE 13，本系列我们下载常用的Java SE 8，下载后按照指示一步步安装。

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-01.png?raw=true)


### 2.配置环境变量

我的电脑点击右键，然后选择属性

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-020.png?raw=true)


- 新建JAVA_HOME环境变量配置为JDK安装目录

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-02.png?raw=true)

- 新建CLASSPATH配置为.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-03.png?raw=true)

- 在PATH环境变量中追加JAVA路径

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-04.png?raw=true)

### 测试JAVA安装

在命令行下执行java -version

```
C:\Users\i>java -version
java version "1.8.0_152"
Java(TM) SE Runtime Environment (build 1.8.0_152-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.152-b11, mixed mode)
```


### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo

### 附

如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star
