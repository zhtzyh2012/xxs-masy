package com.samy.xss.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "xss.safety")
@Data
public class XssUriParam {
    private Set<String> params;
}