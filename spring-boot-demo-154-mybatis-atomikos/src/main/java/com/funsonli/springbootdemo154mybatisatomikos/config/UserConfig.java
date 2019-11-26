package com.funsonli.springbootdemo154mybatisatomikos.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.funsonli.springbootdemo154mybatisatomikos.config.properties.UserProperties;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.funsonli.springbootdemo154mybatisatomikos.mapper.user", sqlSessionTemplateRef = "userSqlSessionTemplate")
public class UserConfig {

    /**
     * 注解@Primary只能有一个地方有 setUniqueResourceName名字需唯一
     * @author Funsonli
     * @date 2019/11/26
     */
    @Bean(name = "userDataSource")
    @Primary
    public DataSource testDataSource(UserProperties userProperties) throws Exception {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(userProperties.getJdbcUrl());
        mysqlXADataSource.setUser(userProperties.getUsername());
        mysqlXADataSource.setPassword(userProperties.getPassword());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("userDataSource");

        return atomikosDataSourceBean;
    }

    /**
     * 设置mapper的xml文件路径
     * @author Funsonli
     * @date 2019/11/26
     */
    @Bean(name = "userSqlSessionFactory")
    @Primary
    public SqlSessionFactory userSqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/user/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        bean.setConfiguration(configuration);
        return bean.getObject();
    }

    @Bean(name = "userSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
