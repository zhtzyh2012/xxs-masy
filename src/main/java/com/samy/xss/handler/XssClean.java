package com.samy.xss.handler;

import com.samy.xss.exception.XssException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.validator.html.*;

import java.io.InputStream;

/**
 * 处理XSS的核心工具类
 *
 * @author zhanghongtu
 */
@Slf4j
public class XssClean {
    /**
     * XSS验证的策略
     */
    private static Policy policy = null;

    /**
     * 获取配置文件的校验策略
     *
     * @return
     * @throws PolicyException
     */
    static {
        if (policy == null) {
            InputStream policyFile = XssClean.class.getResourceAsStream("/antisamy/antisamy-myspace-1.4.4.xml");
            try {
                policy = Policy.getInstance(policyFile);
            } catch (PolicyException e) {
                log.warn("获取验证策略失败,PolicyException=", e);
            }
        }
    }

    /**
     * 根据策略规则进行数据清理
     * 注意原文件会被修改
     *
     * @param source 待验证的源数据
     * @return 清理过的原文件
     */
    public static String xssClean(String source) throws XssException {
        if (StringUtils.isNotEmpty(source)) {
            AntiSamy antiSamy = new AntiSamy();
            try {
                final CleanResults cr = antiSamy.scan(source, policy);
                log.info("before clean source, source is {}", source);
                source = cr.getCleanHTML();
                log.info("after clean source, source is {}", source);
                System.err.println("clean is " + source);

                if (cr.getNumberOfErrors() > 0) {
                    throw new XssException("XSS规则校验不通过");
                }
            } catch (ScanException e) {
                log.error("过滤XSS异常");
                throw new XssException("过滤XSS异常");
            } catch (PolicyException e) {
                log.error("加载XSS规则文件异常: " + e.getMessage());
                throw new RuntimeException("加载XSS规则文件异常: " + e.getMessage());
            }
        }

        return source;
    }

}
