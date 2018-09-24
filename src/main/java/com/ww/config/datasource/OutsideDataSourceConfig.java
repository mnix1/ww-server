package com.ww.config.datasource;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

//@Configuration
////@EnableTransactionManagement
//@PropertySource({"classpath:persistence-multiple-db.properties"})
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "outsideEntityManager",
//        transactionManagerRef = "outsideTransactionManager",
//        basePackages = {"com.ww.repository.outside.book", "com.ww.repository.outside.rival", "com.ww.repository.outside.social", "com.ww.repository.outside.wisie"}
//)
//public class OutsideDataSourceConfig {
//    @Autowired
//    private Environment env;
//
//    @Primary
//    @Bean
//    public LocalContainerEntityManagerFactoryBean outsideEntityManager() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(outsideDataSource());
//        em.setPackagesToScan("com.ww.model.entity.outside.book", "com.ww.model.entity.outside.rival", "com.ww.model.entity.outside.social", "com.ww.model.entity.outside.wisie");
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        em.setJpaPropertyMap(properties);
//        return em;
//    }
//
//    @Primary
//    @Bean
//    public DataSource outsideDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
//        dataSource.setUrl(env.getProperty("outside.jdbc.url"));
//        dataSource.setUsername(env.getProperty("jdbc.user"));
//        dataSource.setPassword(env.getProperty("jdbc.pass"));
//        return dataSource;
//    }
//
//    @Primary
//    @Bean
//    public PlatformTransactionManager outsideTransactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(outsideEntityManager().getObject());
//        return transactionManager;
//    }
//}
