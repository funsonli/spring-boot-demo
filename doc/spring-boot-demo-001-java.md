# Spring Boot 入门样例-001-Java安装

> 本文说明Java在windows下的安装和配置

### 前言

Spring Boot 入门样例-001-Java安装
idea 配置

pom.xml 说明
如何运行

### 1. 下载
下载地址：https://www.oracle.com/technetwork/java/javase/downloads/index.html

目前最新的为Java SE 13，本系列我们下载常用的Java SE 8，下载后按照指示一步步安装。

![图片](images/spring-boot-demo-001-java-01.png?raw=true)


### 2.配置环境变量

我的电脑点击右键，然后选择属性

![图片](images/spring-boot-demo-001-java-020.png?raw=true)


- 新建JAVA_HOME环境变量配置为JDK安装目录

![图片](images/spring-boot-demo-001-java-02.png?raw=true)

- 新建CLASSPATH配置为.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;

![图片](images/spring-boot-demo-001-java-03.png?raw=true)

- 在PATH环境变量中追加JAVA路径

![图片](images/spring-boot-demo-001-java-04.png?raw=true)

### 测试JAVA安装

在命令行下执行java -version

```
C:\Users\i>java -version
java version "1.8.0_152"
Java(TM) SE Runtime Environment (build 1.8.0_152-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.152-b11, mixed mode)
```


### 参考
- http://localhost:8080

### 附
