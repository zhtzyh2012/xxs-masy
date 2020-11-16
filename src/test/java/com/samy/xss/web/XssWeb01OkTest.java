package com.samy.xss.web;

import com.samy.xss.exception.XssException;
import com.samy.xss.handler.XssFilter;
import com.samy.xss.handler.XssInterceptor;
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
public class XssWeb01OkTest {
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new XssFilterWeb())
                .addFilter(new XssFilter())
                .addInterceptors(new XssInterceptor())
                .build();
    }

    @Test
    public void checkRightText() throws Exception {
        String text = "{\"text\":\"<p>hello</p>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verify/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // 得到返回代码
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertEquals(200, status);                //断言，判断返回代码是否正确
        Assert.assertEquals("okay", content);            //断言，判断返回的值是否正确
    }

    @Test(expected = XssException.class)
    public void checkText() throws Exception {
        String text = "{\"text\":\"<script>alert(123);</script>\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/verify/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
