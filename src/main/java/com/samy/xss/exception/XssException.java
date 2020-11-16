package com.samy.xss.exception;

import java.io.IOException;

/**
 * XSS检测发生的XSS异常
 * 注意事项:
 * 1.可以继承ServletException或IOException
 * 2.其他异常发生在拦截器中,会被转化为NestedServletException异常
 * 3.继承其他任何Exception只能使用Exception去捕获
 * 4.具体代码参考FrameworkServlet.processRequest(HttpServletRequest request, HttpServletResponse response)
 * <p>
 * try {
 * doService(request, response);
 * } catch (ServletException | IOException ex) {
 * failureCause = ex;
 * throw ex;
 * } catch (Throwable ex) {
 * failureCause = ex;
 * throw new NestedServletException("Request processing failed", ex);
 * }
 *
 * @author zhanghongtu
 */
public class XssException extends IOException {
    public XssException() {

    }

    public XssException(String message) {
        super(message);
    }
}