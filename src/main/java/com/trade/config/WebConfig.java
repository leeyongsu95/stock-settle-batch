package com.trade.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * DispatcherServlet 전용 웹 계층 설정.
 *
 * REST API 서버이므로 뷰 리졸버(JSP 등)는 설정하지 않고,
 * Jackson HttpMessageConverter를 통한 JSON 직렬화에만 의존한다.
 * (@EnableWebMvc가 Jackson이 클래스패스에 있으면 자동 등록해 준다)
 *
 * Spring 4.2.x에서는 WebMvcConfigurerAdapter 상속이 정석.
 * (Spring 5부터 deprecated 되지만, 4.x에서는 이게 맞다)
 *
 * TODO [Step 2] MessageConverter 커스터마이징, CORS, Interceptor 설정 추가
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
