# Spring Boot入门样例-001-Java和Maven安装配置

> 本文说明Java和Maven在windows下的安装和配置

### 前言

本Spring Boot入门样例准备工作参考：

- Spring Boot入门样例-001-Java和Maven安装配置(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- Spring Boot入门样例-003-idea 安装配置和插件(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- Spring Boot入门样例-005-如何运行(https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)


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

# Maven 安装和配置

下载地址：http://maven.apache.org/download.cgi

目前最新的为3.6.1，下载后解压到C:\。

系统环境变量中添加 M2_HOME 和 MAVEN_HOME，在PATH中添加M2_HOME
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-07.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-001-java-08.png?raw=true)

在命令行下执行mvn -version
``` 
C:\Users\i>mvn -version
Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-05T03:00:29+08:00)
Maven home: C:\apache-maven-3.6.1\bin\..
Java version: 1.8.0_211, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_211\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

### maven仓库

可以在 https://mvnrepository.com/ 查找相关依赖资源

maven依赖包默认从http://repo1.maven.org/maven2/中下载，由于在海外速度比较慢，可以修改.\conf\setting.xml文件中进行如下配置使用阿里云镜像库

``` 
<mirrors>
    <mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>*</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror> 
</mirrors>
```

### 参考
- 源代码地址 https://github.com/funsonli/spring-boot-demo
- https://www.cnblogs.com/happyday56/p/8968328.html


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请记得给我们点赞Star

