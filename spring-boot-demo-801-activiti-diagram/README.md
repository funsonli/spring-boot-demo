# Spring Boot入门样例-801-activiti-diagram整合activiti在线设计

> activiti工作流可以通过网页设计，本demo演示整合activiti实现工作流在线设计。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> idea中需要安装actibpm插件设计，bpmn才能正确显示 插件参考[Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)

### 准备工作

1. 下载activiti包：https://www.activiti.org/get-started，因为6.x版本前端页面尚不完善，我们下载5.22版本，也可以通过网盘 下载链接: https://pan.baidu.com/s/1Qx4LsOl_rmfC_8SdDP1dyg 提取码: m8vg 

2. 解压后再/database/create 文件夹中找到对应数据库的sql文件 创建表，mysql三张表activiti.mysql.create.identity.sql、activiti.mysql.create.history.sql、activiti.mysql.create.engine.sql

3. 解压war/activiti-explorer.war，将diagram-viewer,editor-app,modeler.html文件拷贝到resource/static目录下，将stencilset.json拷贝到resource/目录下

4. 修改editor-app/app-cfg.js，演示版不需要路径
``` 
ACTIVITI.CONFIG = {
	//'contextRoot' : '/activiti-explorer/service',
	'contextRoot' : '',
};
```

5.解压libs\activiti-modeler-5.22.0-sources.jar 文件，将ModelEditorJsonRestResource.java/ModelSaveRestResource.java/StencilsetRestResource.java三个文件拷贝到controller文件

6.新建resources/processes目录

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Activiti 启动器 -->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring-boot-starter-basic</artifactId>
            <version>${activiti.version}</version>
        </dependency>
        <!-- Activiti 流程图 -->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-diagram-rest</artifactId>
            <version>${activiti.version}</version>
        </dependency>
        <!-- Activiti 在线设计 -->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-modeler</artifactId>
            <version>${activiti.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
    username: root
    password: root
    hikari:
      minimum-idle: 5
      maximum-pool-size: 15
      auto-commit: true
      idle-timeout: 30000
      pool-name: DatebookHikariCP
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1

  activiti:
    check-process-definitions: false # activti是否自动部署
    db-identity-used: false #是否使用activti自带的用户体系
    database-schema-update: true #是否每次都更新数据库

```

### 代码解析

ActivitiController.java 如下 先创建模型再跳转到在线设计页面

```
@RestController
@RequestMapping("/activiti")
public class ActivitiController {
    /**
     * 创建模型
     */
    @RequestMapping("/create")
    public void create(HttpServletRequest request, HttpServletResponse response) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            RepositoryService repositoryService = processEngine.getRepositoryService();

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "bootan-test");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            String description = "bootan-test";
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName("bootan-test");
            modelData.setKey("bootan-test123");

            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
            System.out.println("创建模型失败：");
        }
    }

}
```

SpringBootDemo801ActivitiDiagramApplication.java 如下增加exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class, org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}，不使用activiti中的安全验证
```
@SpringBootApplication(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class SpringBootDemo801ActivitiDiagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo801ActivitiDiagramApplication.class, args);
    }

}


```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

``` 
浏览器访问 http://localhost:8080/activiti/create
创建模型后会自动跳转到  http://localhost:8080/modeler.html?modelId=12503
```

![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-801-01.png?raw=true)

设计完成后点击左上方的保存，官方的代码ModelEditorJsonRestResource.java会保存但是还是会报错，可以参考[bootan的actitivi工作流](https://github.com/funsonli/bootan/blob/master/src/main/java/com/funsonli/bootan/module/activiti/controller/modeler/ModelSaveRestResource.java)

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan
- Activiti官网 https://www.activiti.org/
- Activiti表结构 https://blog.csdn.net/hj7jay/article/details/51302829

### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

