package com.funsonli.springbootdemo020properties.controller.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.student")
public class StudentProperties {
    private String name;
    private Integer age;
}
