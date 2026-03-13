package com.trade.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 설정.
 * 메타 테이블 없이 인메모리로 운영 — 짧은 주기 배치라 재실행이 실용적.
 * 트랜잭션 매니저는 JPA 쪽으로 통일하여 배치 내 JPA/MyBatis 혼용 처리를 보장한다.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public void setDataSource(DataSource dataSource) {
        // DataSource 안 넘기면 인메모리로 동작
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }
}
