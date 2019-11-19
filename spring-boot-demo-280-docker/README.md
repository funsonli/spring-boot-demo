# Spring Boot入门样例-280-docker使用Docker容器运行

> 使用docker容器可以大大简化线上运营部署。本demo演示如何演示如何使用Docker容器运行Spring Boot项目。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.build.timestamp.format>yyyyMMddHHmmss</maven.build.timestamp.format>
        <java.version>1.8</java.version>
        <dockerfile-version>1.4.9</dockerfile-version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <build>
        <finalName>spring-boot-demo-280-docker</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>${dockerfile-version}</version>
                <configuration>
                    <repository>${project.build.finalName}</repository>
                    <tag>${project.version}</tag>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### 配置文件

resources/application.yml配置内容
```
server:
  port: 8080

```

Dockfile文件
``` 
# 基础镜像
FROM openjdk:8-jdk-alpine

# 作者信息
MAINTAINER "funsonli@163.com"

# 添加一个存储空间
VOLUME /tmp

# 8080端口 和 yml文件保持一致
EXPOSE 8080

# JAR变量
ARG JAR_FILE=spring-boot-demo-280-docker.jar

# 往容器中添加jar包
ADD ${JAR_FILE} app.jar

# 启动镜像自动运行程序
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]
```

### 代码解析

其他代码解析参考 spring-boot-demo-040-jpa 模块。

SpringBootDemo280DockerApplication.java 如下 访问根路径显示hello docker
``` 
@RestController
@SpringBootApplication
public class SpringBootDemo280DockerApplication {

    @GetMapping("/")
    public String index() {
        return "hello docker";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo280DockerApplication.class, args);
    }

}
```

### 编译

使用maven进行编译

```
D:\java\spring-boot-demo\spring-boot-demo-280-docker>mvn clean package -Dmaven.test.skip=true

[INFO] --- maven-jar-plugin:3.1.2:jar (default-jar) @ spring-boot-demo-280-docker ---
[INFO] Building jar: D:\java\spring-boot-demo\spring-boot-demo-280-docker\target\spring-boot-demo-280-docker.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:2.2.0.RELEASE:repackage (repackage) @ spring-boot-demo-280-docker ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.809 s
[INFO] Finished at: 2019-10-24T16:22:35+08:00
[INFO] ------------------------------------------------------------------------
```

### 部署运行

将target\spring-boot-demo-280-docker.jar 和 Dockerfile 文件上传到有docker环境

本demo在Centos环境下，centos安装docker请参考 https://www.runoob.com/docker/centos-docker-install.html

```
# docker build -t spring-boot-demo-docker .
# docker images
REPOSITORY                            TAG                 IMAGE ID            CREATED             SIZE
spring-boot-demo-docker              latest              7f67e65c32be        About an hour ago   122 MB

# docker run -d -p 80:8080 spring-boot-demo-docker
```
将80端口指向docker image的8080端口，即Spring Boot项目中的端口

``` 
浏览器访问 http://112.123.234.22/   您的服务器ip
hello docker
```


### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

