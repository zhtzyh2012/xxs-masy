package com.samy.xss.wrapper;

import com.samy.xss.handler.XssClean;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

/**
 * 基础包装类
 *
 * @author zhanghongtu
 */
public class BaseReqWrapper extends HttpServletRequestWrapper {

    public BaseReqWrapper(HttpServletRequest request) {
        super(request);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> request_map = super.getParameterMap();
        Iterator iterator = request_map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me = (Map.Entry) iterator.next();
            String[] values = (String[]) me.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssClean(values[i]);
            }
        }
        return request_map;
    }

    @Override
    public String[] getParameterValues(String param) {
        String[] arrayOfString1 = super.getParameterValues(param);
        if (arrayOfString1 == null)
            return null;
        int i = arrayOfString1.length;
        String[] arrayOfString2 = new String[i];
        for (int j = 0; j < i; j++)
            arrayOfString2[j] = xssClean(arrayOfString1[j]);
        return arrayOfString2;
    }

    @Override
    public String getParameter(String param) {
        String str = super.getParameter(param);
        return str == null ? null : xssClean(str);
    }

    @Override
    public String getHeader(String param) {
        String str = super.getHeader(param);
        return str == null ? null : xssClean(str);
    }

    @SneakyThrows
    private String xssClean(String value) {
        return XssClean.xssClean(value);
    }

}
