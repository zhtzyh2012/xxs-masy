package com.samy.xss.handler;

/**
 * 进行xss验证时使用的常量
 *
 * @author zhanghongtu
 */
public class XssConst {

    /**
     * XSS校验中安全字段,不需要做校验
     */
    public static final String XSS_PARAMETER_SECURITY = "xss_parameter_security";

    /**
     * 目前只有application/json需要走body
     */
    public static final String JSON_CONTENT_TYPE = "application/json";
}
