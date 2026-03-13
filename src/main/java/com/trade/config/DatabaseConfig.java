package com.trade.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DB 연결 설정 — HikariCP 사용.
 * 짧은 트랜잭션이 빈번한 매매 시스템 특성상 DBCP2 대비 연결 획득 성능이 유리하다.
 *
 * [운영 시] DB 접속 정보는 Jasypt 암호화 또는 Jenkins 인증 정보 주입으로 전환 예정.
 */
@Configuration
public class DatabaseConfig {

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.url}")
    private String jdbcUrl;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.pool.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${db.pool.minimum-idle}")
    private int minimumIdle;

    @Value("${db.pool.connection-timeout}")
    private long connectionTimeout;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        return new HikariDataSource(config);
    }
}
