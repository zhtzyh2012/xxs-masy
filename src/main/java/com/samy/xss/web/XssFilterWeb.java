package com.samy.xss.web;

import com.samy.xss.entity.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
public class XssFilterWeb {
    /**
     * 正常验证接口
     */
    @RequestMapping(value = "/verify/xss", method = POST)
    public String verifyXss(@RequestBody Text xss) {
        log.info("res is {}", xss.getText());
        return "okay";
    }

    /**
     * 验证**的拦截范围
     */
    @RequestMapping(value = "/api/verify2/xss", method = POST)
    public String apiVerifyXss(@RequestBody Text xss) {
        log.info("res is {}", xss.getText());
        return "okay";
    }

    /**
     * 验证*的拦截范围
     */
    @RequestMapping(value = "/ok/verify3/xss", method = POST)
    public String okVerifyXss(@RequestBody Text xss) {
        log.info("res is {}", xss.getText());
        return "okay";
    }

    /**
     * 验证?的拦截范围
     */
    @RequestMapping(value = "/okay/verify4/xss", method = POST)
    public String okayVerifyXss(@RequestBody Text xss) {
        log.info("res is {}", xss.getText());
        return "okay";
    }

    /**
     * 正常验证接口
     */
    @RequestMapping(value = "/api/verify/xss2")
    public String verifyXss2(@RequestBody Text xss) {
        log.info("res is {}", xss.getText());
        return "okay";
    }

}
