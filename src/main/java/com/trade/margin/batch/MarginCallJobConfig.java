package com.trade.margin.batch;

import com.trade.margin.dto.MarginCallTargetDto;
import com.trade.margin.repository.MarginCallMapper;
import com.trade.order.entity.TradeOrder;
import com.trade.order.repository.TradeOrderRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 반대매매 배치 — 장 마감 후 1회 실행.
 * Reader: MyBatis 집계 쿼리 (인덱스 활용)
 * Writer: JPA 매도 주문 생성 (엔티티 활용)
 */
@Configuration
public class MarginCallJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MarginCallMapper marginCallMapper;

    @Autowired
    private TradeOrderRepository tradeOrderRepository;

    @Bean
    public Job marginCallJob() {
        return jobBuilderFactory.get("marginCallJob")
                .start(marginCallStep())
                .build();
    }

    @Bean
    public Step marginCallStep() {
        return stepBuilderFactory.get("marginCallStep")
                .<MarginCallTargetDto, TradeOrder>chunk(50)
                .reader(marginCallReader())
                .processor(marginCallProcessor())
                .writer(marginCallWriter())
                .build();
    }

    @Bean
    public MarginCallReader marginCallReader() {
        return new MarginCallReader(marginCallMapper);
    }

    @Bean
    public MarginCallProcessor marginCallProcessor() {
        return new MarginCallProcessor();
    }

    @Bean
    public MarginCallWriter marginCallWriter() {
        return new MarginCallWriter(tradeOrderRepository);
    }
}
