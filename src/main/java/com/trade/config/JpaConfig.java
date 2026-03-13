package com.trade.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA + 트랜잭션 설정.
 * JpaTransactionManager 하나로 JPA/MyBatis 트랜잭션을 묶어서 관리한다.
 * 같은 DB 연결을 쓰면 MyBatis 쿼리도 같은 트랜잭션으로 묶인다.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.trade")
public class JpaConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${jpa.hibernate.dialect}")
    private String dialect;

    @Value("${jpa.hibernate.show-sql}")
    private String showSql;

    @Value("${jpa.hibernate.format-sql}")
    private String formatSql;

    @Value("${jpa.hibernate.hbm2ddl-auto}")
    private String hbm2ddlAuto;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.trade");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", dialect);
        props.setProperty("hibernate.show_sql", showSql);
        props.setProperty("hibernate.format_sql", formatSql);
        props.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        emf.setJpaProperties(props);

        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        return tm;
    }
}
