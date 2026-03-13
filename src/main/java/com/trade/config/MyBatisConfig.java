package com.trade.config;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * MyBatis 설정 — annotationClass 필터 필수 (JPA Repository 충돌 방지).
 */
@Configuration
@MapperScan(
        basePackages = "com.trade",
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        factory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mybatis/mapper/**/*.xml")
        );
        factory.setTypeAliasesPackage("com.trade");
        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
