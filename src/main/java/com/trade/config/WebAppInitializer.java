package com.trade.config;

import javax.servlet.Filter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * web.xml 없이 서블릿 컨텍스트를 부팅하는 Java Config 진입점.
 *
 * Servlet 3.0+ 스펙의 ServletContainerInitializer 메커니즘 덕분에
 * Tomcat 기동 시 이 클래스를 자동으로 감지한다.
 * AbstractAnnotationConfigDispatcherServletInitializer를 상속하면
 * Root / Servlet 컨텍스트 분리가 깔끔하게 되어서 선택.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Root ApplicationContext:
     * 서비스, 리포지토리, 배치 Job 등 비즈니스 계층 빈을 여기서 관리한다.
     * DB 커넥션 풀, 트랜잭션 매니저 같은 인프라 설정도 이 컨텍스트 소속.
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ AppConfig.class };
    }

    /**
     * Servlet ApplicationContext:
     * 컨트롤러, 인터셉터, MessageConverter 등 웹 계층 전용 빈만 관리.
     * Root 컨텍스트의 빈은 참조 가능하지만 반대는 불가 — 의존 방향에 주의.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }

    /**
     * 한글 파라미터 깨짐 방지용 인코딩 필터.
     * 금융 시스템에서 종목명이나 메모 필드에 한글이 들어올 수 있어서 필수.
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{ encodingFilter };
    }
}
