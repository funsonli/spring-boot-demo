package com.funsonli.springbootdemo063templateenjoy.config;

import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author Funson
 * @date 2019/10/23
 */
@Configuration
public class EnjoyConfig {
	@Bean
	public JFinalViewResolver jFinalViewResolver() {
		JFinalViewResolver jfr = new JFinalViewResolver();
		jfr.setDevMode(false);
		jfr.setSourceFactory(new ClassPathSourceFactory());
		//设置模板根路径
		jfr.setPrefix("/templates/");
		//设置模板后缀
		jfr.setSuffix(".html");
		jfr.setContentType("text/html;charset=UTF-8");
		jfr.setOrder(0);

		return jfr;
	}
}
