package com.samy.xss.handler;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class XssFilterConfig {
    private XssUri xssUri;
    private XssFilter xssFilter;

    public XssFilterConfig(XssUri xssUri, XssFilter xssFilter) {
        this.xssUri = xssUri;
        this.xssFilter = xssFilter;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy(xssFilter));
        registration.setUrlPatterns(xssUri.getUri());
        registration.setName("xssFilter");
        return registration;
    }
}
