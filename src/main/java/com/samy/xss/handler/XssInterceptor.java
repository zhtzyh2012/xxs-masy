package com.samy.xss.handler;

import com.samy.xss.exception.XssException;
import com.samy.xss.wrapper.JsonReqWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class XssInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws XssException {
        if (request instanceof JsonReqWrapper) {
            JsonReqWrapper jsonReqWrapper = (JsonReqWrapper) request;
            // 获取json字符串
            String origin = jsonReqWrapper.getBodyString();
            log.info("XssJsonInterceptor before data {}", origin);

            // 对Json字符串数据clean
            String source = XssClean.xssClean(origin);
            log.info("XssJsonInterceptor after data {}", source);
        }

        // 2.对url链路上的参数clean
        request.getParameterMap();

        return true;
    }
}
