package com.samy.xss.web;

import com.samy.xss.handler.XssFilter;
import com.samy.xss.handler.XssInterceptor;
import com.samy.xss.exception.XssException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class XssWeb03ErrorTest {
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
    public void checkText10() throws Exception {
        String name = "/Users/zhanghongtu/Documents/xss-new.txt";
        List<String> texts = read(name);

        List<String> pass = new ArrayList<>();

        int number = 0;
        int ok = 0;
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            System.err.println("change before" + text);
            String text2 = text.replace("\"", "\\\"");
            System.err.println("change after" + text2);

            StringBuffer buffer = new StringBuffer("{\"text\":\"").append(text2).append("\"");
            try {
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verify/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buffer.toString())
                        .accept(MediaType.TEXT_HTML_VALUE))
                        // .andDo(MockMvcResultHandlers.print())
                        .andReturn();
                // 得到返回代码
                int status = mvcResult.getResponse().getStatus();
                String content = mvcResult.getResponse().getContentAsString();
                System.err.println("status: " + status + " context: " + content);
                pass.add(text);
            } catch (XssException ex) {
                number += 1;
            }
        }

        log.info("共计出现{}异常数据", number);
        log.info("共计出现{}正常数据", ok);

        System.out.println("-----------------------------------------------------");
        pass.stream().forEach(System.err::println);
    }

    @SneakyThrows
    private List<String> read(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> texts = new ArrayList<>();

        File file = new File(name);
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                texts.add(temp);
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("阅读{}行xss验证数据", line);
        return texts;
    }


    @Test
    public void checkText11() throws Exception {
        String text = "&#X000003c;";
        StringBuffer buffer = new StringBuffer("{\"text\":\"").append(text).append("\"");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verify/xss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buffer.toString())
                .accept(MediaType.TEXT_HTML_VALUE))
                // .andDo(MockMvcResultHandlers.print())
                .andReturn();
        // 得到返回代码
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
    }

}
