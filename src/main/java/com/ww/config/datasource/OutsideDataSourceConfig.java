package com.ww.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
//@EnableTransactionManagement
@PropertySource({"classpath:persistence-multiple-db.properties"})
@EnableJpaRepositories(
        entityManagerFactoryRef = "outsideEntityManager",
        transactionManagerRef = "outsideTransactionManager",
        basePackages = {"com.ww.repository.outside.book", "com.ww.repository.outside.rival", "com.ww.repository.outside.social", "com.ww.repository.outside.wisie"}
)
public class OutsideDataSourceConfig {
    @Autowired
    private Environment env;

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean outsideEntityManager(DataSourceProperties cloudSqlDataSourceProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(cloudSqlDataSourceProperties.initializeDataSourceBuilder().build());
        em.setPackagesToScan("com.ww.model.entity.outside.book", "com.ww.model.entity.outside.rival", "com.ww.model.entity.outside.social", "com.ww.model.entity.outside.wisie");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("outside.dialect"));
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.temp.use_jdbc_metadata_defaults"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager outsideTransactionManager(DataSourceProperties cloudSqlDataSourceProperties) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(outsideEntityManager(cloudSqlDataSourceProperties).getObject());
        return transactionManager;
    }
}
