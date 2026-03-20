package com.trade.settlement.batch;

import com.trade.settlement.dto.ExecutionSettlementDto;
import com.trade.settlement.dto.SettlementInsertDto;
import com.trade.settlement.repository.SettlementMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 정산 배치 Job 구성.
 * 짧은 주기로 실행되는 마이크로 배치 — 매회 100건씩 Chunk 처리.
 */
@Configuration
public class SettlementJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SettlementMapper settlementMapper;

    @Bean
    public Job settlementJob() {
        return jobBuilderFactory.get("settlementJob")
                .start(settlementStep())
                .build();
    }

    @Bean
    public Step settlementStep() {
        return stepBuilderFactory.get("settlementStep")
                .<ExecutionSettlementDto, SettlementInsertDto>chunk(100)
                .reader(settlementItemReader())
                .processor(settlementProcessor())
                .writer(settlementWriter())
                .build();
    }

    @Bean
    public SettlementItemReader settlementItemReader() {
        return new SettlementItemReader(settlementMapper);
    }

    @Bean
    public SettlementProcessor settlementProcessor() {
        return new SettlementProcessor();
    }

    @Bean
    public SettlementWriter settlementWriter() {
        return new SettlementWriter(settlementMapper);
    }
}
