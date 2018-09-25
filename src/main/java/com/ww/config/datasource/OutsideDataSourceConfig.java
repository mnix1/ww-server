package com.ww.config.datasource;

import com.google.common.base.Preconditions;
import com.ww.helper.EnvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

import static com.ww.helper.EnvHelper.outsideCreateSchemaDb;
import static com.ww.helper.EnvHelper.outsideDbSchema;

@Configuration
@PropertySource({"classpath:persistence-multiple-db.properties"})
//@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "outsideEntityManager",
        transactionManagerRef = "outsideTransactionManager",
        basePackages = {"com.ww.repository.outside.book", "com.ww.repository.outside.rival", "com.ww.repository.outside.social", "com.ww.repository.outside.wisie"}
)
@Profile({EnvHelper.DB_PROD, EnvHelper.DB_UAT})
public class OutsideDataSourceConfig {
    @Autowired
    private Environment env;

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean outsideEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(outsideDataSource());
        em.setPackagesToScan("com.ww.model.entity.outside.book", "com.ww.model.entity.outside.rival", "com.ww.model.entity.outside.social", "com.ww.model.entity.outside.wisie");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", outsideCreateSchemaDb(env));
        properties.put("hibernate.dialect", env.getProperty("outside.dialect"));
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.temp.use_jdbc_metadata_defaults"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public DataSource outsideDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("outside.driverClassName")));
        dataSource.setUrl(env.getProperty("outside.url"));
        dataSource.setUsername(env.getProperty("outside.user"));
        dataSource.setPassword(env.getProperty("outside.pass"));
        dataSource.setSchema(outsideDbSchema(env));
        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager outsideTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(outsideEntityManager().getObject());
        return transactionManager;
    }
}
