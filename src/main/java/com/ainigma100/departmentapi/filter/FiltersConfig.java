package com.ainigma100.departmentapi.filter;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class FiltersConfig {

    private final RateLimitingFilter rateLimitingFilter;
    private final LoggingFilter loggingFilter;



    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterBean() {

        final FilterRegistrationBean<LoggingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(loggingFilter);
        filterBean.addUrlPatterns("/api/v1/*");
        // Lower values have higher priority
        filterBean.setOrder(2);

        return filterBean;
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilterFilterRegistrationBean() {

        final FilterRegistrationBean<RateLimitingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(rateLimitingFilter);
        filterBean.addUrlPatterns("/api/v1/*");
        // Lower values have higher priority
        filterBean.setOrder(1);

        return filterBean;
    }


}
