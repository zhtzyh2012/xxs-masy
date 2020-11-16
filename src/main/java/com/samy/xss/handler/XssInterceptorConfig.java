package com.samy.xss.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
public class XssInterceptorConfig implements WebMvcConfigurer {
    private XssUri xssUri;

    public XssInterceptorConfig(XssUri xssUri) {
        this.xssUri = xssUri;
    }

    @Bean
    public XssInterceptor getXssInterceptor() {
        return new XssInterceptor();
    }

    /**
     * 注册拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (Objects.nonNull(xssUri) && Objects.nonNull(xssUri.getUri()) && xssUri.getUri().size() > 0) {
            registry.addInterceptor(getXssInterceptor())
                    .addPathPatterns(xssUri.getUri());
        }
    }

}