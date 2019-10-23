# Spring Boot入门样例-005-如何运行

> 本文说明本项目如何下载和运行

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java和Maven安装配置(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)
- Spring Boot入门样例-007-pom.xml说明(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-007-pom.md)


### 1. 克隆项目源代码
项目源代码地址为：https://github.com/funsonli/spring-boot-demo/

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-01.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-03.png?raw=true)


### 2.配置项目

点击【File】【Project Structure】，设置Project SDK为1.8

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-030.png?raw=true)


### 3.加载项目依赖

idea右下角会弹出如下框，点击右侧的Enable Auto-Import，自动加载项目相关依赖jar

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-07.png?raw=true)

### 4.运行项目

因为项目包含众多的模块，所以不能直接点击有上角绿色按钮，而是需要到具体模块下面的启动文件中启动，如下图所以，打开SpringBootDemo010HelloWorldApplication.java文件，点击左侧绿色三角形按钮运行（两个按钮都可以）。

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-09.png?raw=true)

查看日志，系统自动监听8080端口

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-11.png?raw=true)


### 测试结果


浏览器下访问 http://localhost:8080/

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-005-run-13.png?raw=true)


### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

