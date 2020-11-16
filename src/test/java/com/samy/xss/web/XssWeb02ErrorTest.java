package com.samy.xss.web;

import com.samy.xss.handler.XssFilter;
import com.samy.xss.handler.XssInterceptor;
import com.samy.xss.exception.XssException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class XssWeb02ErrorTest {
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new XssFilterWeb())
                .addFilter(new XssFilter())
                .addInterceptors(new XssInterceptor())
                .build();
    }

    @Test(expected = XssException.class)
    public void checkText() throws Exception {
        String text = "{\"text\":\"<script>alert(123);</script>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/verify2/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // 得到返回代码
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回代码和文本是否正确
        Assert.assertEquals(200, status);
        Assert.assertEquals("okay", content);
    }

    /**
     * 请求拦截地址的通配符限制
     * 验证/**,匹配多级路径
     * 参考配置文件中: xss.verify.uri[4]=/api/**
     *
     * @throws Exception
     */
    @Test
    public void checkTextTwoStars() throws Exception {
        String text = "{\"text\":\"<h1>alert(999);</h1>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/verify2/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // 得到返回代码
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // 判断返回代码和返回值是否正确
        Assert.assertEquals(200, status);
        Assert.assertEquals("okay", content);
    }

    /**
     * 请求拦截地址的通配符限制
     * 验证/*,匹配单级路径
     * 参考配置文件: xss.verify.uri[4]=/ok/* /xss
     *
     * @throws Exception
     */
    @Test
    public void checkTextOneStar() throws Exception {
        String text = "{\"text\":\"<h2>h3(333);</h2>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/ok/verify3/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // 判断返回代码和返回值是否正确
        Assert.assertEquals(200, status);
        Assert.assertEquals("okay", content);
    }

    /**
     * 请求拦截地址的通配符限制
     * 验证单个字符?,匹配单级路径
     *
     * @throws Exception
     */
    @Test
    public void checkTextLikeThink() throws Exception {
        String text = "{\"text\":\"<p>eval('hello');</p>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/okay/verify4/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        //判断返回代码和返回值是否正确
        Assert.assertEquals(200, status);
        Assert.assertEquals("okay", content);
    }
}
