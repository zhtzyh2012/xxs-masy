package com.samy.xss.handler;

import com.samy.xss.wrapper.JsonReqWrapper;
import com.samy.xss.wrapper.ParamReqWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * XSS过滤器
 * 核心作用在于将流中的数据保存到缓存中,可以多次重复使用
 *
 * @author zhanghongtu
 */
@Slf4j
@WebFilter(filterName = "xssFilter")
@Component
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req2 = (HttpServletRequest) request;
        String contentType = request.getContentType();

        if (XssConst.JSON_CONTENT_TYPE.equals(contentType)) {
            // 处理当前参数在body中
            HttpServletRequestWrapper wrapper = new JsonReqWrapper(req2);
            chain.doFilter(wrapper, response);
        } else {
            // 处理当前参数在param中
            ParamReqWrapper wrapper = new ParamReqWrapper(req2);
            chain.doFilter(wrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}
