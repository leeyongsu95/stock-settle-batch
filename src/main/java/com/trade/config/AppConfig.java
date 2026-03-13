package com.trade.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * 루트 애플리케이션 컨텍스트 설정.
 *
 * 컨트롤러를 제외한 서비스 / 리포지토리 / 배치 빈을 스캔한다.
 * 여기서 @Controller를 명시적으로 제외하지 않으면
 * Root <-> Servlet 양쪽 컨텍스트에 빈이 이중 등록되어
 * 트랜잭션 AOP가 안 먹히는 미묘한 버그가 터진다 (경험담).
 *
 * TODO [Step 2] DataSource, JPA, MyBatis, Redis, Batch 인프라 Config 추가
 */
@Configuration
@ComponentScan(
        basePackages = "com.trade",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Controller.class
        )
)
public class AppConfig {

}
