package com.example.demo1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) @SpringBootTest public class Demo1ApplicationTests {

    @Autowired private WebApplicationContext webContext;

    private MockMvc mockMvc;

    @Before public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();

    }

    /**
     * 测试结果1
     */
    @Test public void testOneThread() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> testOne()).start();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        testOneResult();
    }

    private void testOne() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/testOne")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    private void testOneResult() {
        try {
            System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/testOneResult"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    /**
     * 测试结果2
     */
    @Test public void testTwoThread() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> testTwo()).start();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        testTwoResult();
    }


    private void testTwo() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/testTwo")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    private void testTwoResult() {
        try {
            System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/testTwoResult"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    /**
     * 测试结果3
     */
    @Test public void testThreeThread() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> testThree()).start();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        testThreeResult();
    }


    private void testThree() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/testThree")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    private void testThreeResult() {
        try {
            System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/testThreeResult"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    /**
     * 测试结果4
     */
    @Test public void testFourThread() throws InterruptedException{
        int threadNum = 100;
        DemoApi.count = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> testFour()).start();
        }
        DemoApi.count.await();
        testFourResult();
    }


    private void testFour() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/testFour")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

    private void testFourResult() {
        try {
            System.out.println(mockMvc.perform(MockMvcRequestBuilders.get("/testFourResult"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString());
        } catch (Exception e) {
            System.out.println("test filed");
        }
    }

}
