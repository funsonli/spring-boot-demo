package com.funsonli.springbootdemo151jpamultisource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager",
        basePackages = {"com.funsonli.springbootdemo151jpamultisource.dao.order"})
public class OrderConfig {

    @Autowired
    @Qualifier("orderDataSource")
    private DataSource orderDataSource;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Bean(name = "orderEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return orderEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Resource
    private JpaProperties jpaProperties;

    @Bean(name = "orderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder
                .dataSource(orderDataSource)
                .packages("com.funsonli.springbootdemo151jpamultisource.entity.order")
                .persistenceUnit("orderPersistenceUnit")
                .properties(properties)
                .build();
    }

    @Bean(name = "orderTransactionManager")
    PlatformTransactionManager orderTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(orderEntityManagerFactory(builder).getObject());
    }

}
