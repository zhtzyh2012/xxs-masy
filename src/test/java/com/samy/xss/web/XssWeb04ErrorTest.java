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
public class XssWeb04ErrorTest {
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
    public void checkTextOne() throws Exception {
        String text = "{\"text\":\"<h1>alert(123);</h1>\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/verify/xss2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(text)
                .param("name", "<script>")
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

    @Test(expected = XssException.class)
    public void checkTextTwo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/verify/xss2")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "<script211>")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // 得到返回代码
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        // 断言，判断返回代码和文本是否正确
        Assert.assertEquals(200, status);
        Assert.assertEquals("okay", content);
    }

}
