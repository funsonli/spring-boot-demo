# Spring Boot入门样例-170-i18n国际化多语言切换

> 当网站有多个国家用户访问时，如何让他们切换自己熟悉的语言。本demo演示在Spring Boot中国际化。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> 模板渲染样例请参考，[Spring Boot入门样例-060-thymeleaf](https://github.com/funsonli/spring-boot-demo/tree/master/spring-boot-demo-060-template-thymeleaf) 本demo是在该模板基础上扩展的

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>webjars-locator-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.3.1</version>
        </dependency>

        <dependency><!--jQuery国际化插件-->
            <groupId>org.webjars.bower</groupId>
            <artifactId>jquery-i18n-properties</artifactId>
            <version>1.2.7</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容  basename配置语言路径在resource/messages/messages.properties 等文件
```
spring:
  messages:
    basename: i18n/messages
    encoding: UTF-8
    cache-duration: 3600
  thymeleaf:
    cache: false
    servlet:
      content-type: text/html
    mode: HTML
    encoding: UTF-8
```

### 代码解析
该项目有很多目录，分别说明如下：

- config目录为控制器文件
- controller目录为控制器文件
- entity目录为实体目录，对应表格中的字段（本样例不查询数据库）
- resource/messages语言文件路径
- resources/templates对应模板文件路径
- resources/static静态文件路径，包括css和images图片

以下解释和国际化相关的文件

LocaleConfig.java 为，配置默认语言为英语  其中路径参数lang表示切换语言的参数名
``` 
@Configuration
public class LocaleConfig {
    /**
     * 默认解析器 其中locale表示默认语言
     * @author funsonli
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        // 默认语言为英语
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    /**
     * 默认拦截器 其中lang表示切换语言的参数名
     * @author funsonli
     */
    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName("lang");
                registry.addInterceptor(localeInterceptor);
            }
        };
    }
}

```

SiteController.java 如下
``` 
@Controller
public class SiteController {

    @GetMapping({"", "/", "index"})
    public String index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user  == null) {
            return "redirect:/login";
        }
        request.setAttribute("user", user);
        return "site/index";
    }

    @GetMapping("/login")
    public String login() {
        return "site/login";
    }

    @PostMapping("/login")
    public String save(@ModelAttribute User modelAttribute, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "binding error";
        }
        request.getSession().setAttribute("user", modelAttribute);
        return "redirect:/";
    }
}

```

1. index方法先判断是否登录，否则跳转login
2. get的login方法显示resources/templates/site/login.html文件，该文件会引入resources/templates/common/head.html，以及使用resources/static中的css和图片文件
3. 登录后显示resources/templates/site/index.html中内容


head.html 和语言有关的变量使用 #{xxx}来读取，不是${xxx}来读取
``` 
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="header">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>欢迎使用Spring Boot入门样例之thymeleaf</title>
	<link type="text/css" rel="stylesheet" href="/css/style.css" />
	<script th:src="@{/webjars/jquery/3.3.1/jquery.min.js}"></script>
	<script th:src="@{/webjars/jquery-i18n-properties/jquery.i18n.properties.min.js}"></script>
	<script th:inline="javascript">
		//获取应用路径
		var ROOT = [[${#servletContext.contextPath}]];

		//获取默认语言
		var LANG_COUNTRY = [[${#locale.language+'_'+#locale.country}]];
	</script>
</head>
</html>
```

index.html 在script中通过在参数后加 ?lang=xxx 来切换语音
``` 
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<header th:replace="~{common/head :: header}"></header>
<body>
<div class="header">
	<a href="/"><img src="/images/logo.png"></a><span th:text="#{spring_boot}">
</div>
<div class="content">
	<select id="locale">
		<option value="zh_CN">中文简体</option>
		<option value="en_US">English</option>
	</select>

	<span th:text="#{welcome}"></span><span th:text="${user.name}"></span>
</div>
<script th:inline="javascript">
	//选中语言
	$("#locale").find('option[value="' + LANG_COUNTRY + '"]').attr('selected', true);
	//切换语言
	$("#locale").change(function () {
		$.get(ROOT + '/?lang=' + $("#locale").val(), function () {
			location.reload();
		});
	});
</script>
</body>
</html>
```
login.html 在script中通过在参数后加 ?lang=xxx 来切换语音
``` 
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<header th:replace="~{common/head :: header}"></header>
<body>
<div class="header">
	<a href="/"><img src="/images/logo.png"></a><span th:text="#{spring_boot}">
</div>

<div class="content">
	<select id="locale">
		<option value="zh_CN">中文简体</option>
		<option value="en_US">English</option>
	</select>
	<form action="/login" method="post">
		<input type="text" name="name" th:placeholder="#{user.name}"/><br>
		<input type="password" name="password" th:placeholder="#{user.password}"/><br>
		<input type="submit" th:value="#{user.login}">
	</form>
</div>
<script th:inline="javascript">
	//选中语言
	$("#locale").find('option[value="' + LANG_COUNTRY + '"]').attr('selected', true);
	//切换语言
	$("#locale").change(function () {
		$.get(ROOT + '/?lang=' + $("#locale").val(), function () {
			location.reload();
		});
	});
</script>

</body>
</html>
```
### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

```
浏览器访问 http://localhost:8080/
浏览器访问 http://localhost:8080/login

```
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-170-01.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-170-03.png?raw=true)
![图片](https://raw.githubusercontent.com/funsonli/spring-boot-demo/master/doc/images/spring-boot-demo-170-05.png?raw=true)

### 参考
- Spring Boot入门样例源代码地址 https://github.com/funsonli/spring-boot-demo
- Bootan源代码地址 https://github.com/funsonli/bootan


### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

