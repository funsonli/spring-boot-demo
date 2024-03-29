# Spring Boot入门样例-003-idea 安装配置和插件

> 本文说明Java编辑器Idea的安装和本项目所用到的插件

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### 下载安装
下载地址：https://www.jetbrains.com/idea/download/

下载后双击exe文件按照指示一步步安装。

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-003-idea-01.png?raw=true)


### 常用插件

- 启动idea选择【File】【Setting】，在弹出框左侧选择【Plugins】，在右侧【Marketplace】中搜索lombok，点击【Install】安装。

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-003-idea-03.png?raw=true)


- 安装阿里巴巴Java编程规范，搜索Alibaba Java

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-003-idea-05.png?raw=true)


### 新项目无法正常启动

1. 右键子目录的 pom.xml，选择Add as Maven Project
2. 如果目录有问题，选择 File - Project Structure - Modules中设置路径
2. 在右侧Maven中，双击 项目 - Lifecycle - clean 和 install重新下载相关依赖

### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

