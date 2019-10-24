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