package com.samy.xss.wrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理参数在param中
 *
 * @author zhanghongtu
 */
public class ParamReqWrapper extends BaseReqWrapper {

    public ParamReqWrapper(HttpServletRequest request) {
        super(request);
    }

}
