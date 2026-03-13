package com.trade.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 서블릿 컨텍스트 — @Controller만 스캔.
 * REST API 전용이라 화면 없이 JSON 변환만 사용한다.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.trade",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Controller.class
        ),
        useDefaultFilters = false
)
public class WebConfig extends WebMvcConfigurerAdapter {

}
