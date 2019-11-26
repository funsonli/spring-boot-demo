package com.funsonli.springbootdemo154mybatisatomikos.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
@ConfigurationProperties(prefix = "spring.datasource.order")
public class OrderProperties {
    @Value("${spring.datasource.order.jdbc-url}")
    private String jdbcUrl;

    private String username;

    private String password;
}
