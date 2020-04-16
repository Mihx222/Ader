package com.ader.backend.config.logging;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {

    @Bean
    public FilterRegistrationBean<AderRequestResponseLoggingFilter> requestResponseLoggingFilterRegistrationBean() {
        FilterRegistrationBean<AderRequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AderRequestResponseLoggingFilter());
        return registrationBean;
    }
}
