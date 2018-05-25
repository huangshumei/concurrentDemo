package com.example.demo1;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DemoApi {

    private static Integer num = 0;

    private static volatile Integer volNum = 0;

    public static CountDownLatch count;

    public static AtomicInteger atomic = new AtomicInteger(0);

    /**
     * 测试1
     */
    @RequestMapping(value = "/testOne",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testOne(){
        num++;
        count.countDown();
    }

    @RequestMapping(value = "/testOneResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testOneResult(){
        return "num计算结果：" + num;
    }


    /**
     * 测试2
     */
    @RequestMapping(value = "/testTwo",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testTwo(){
        num++;
        volNum++;
        count.countDown();
    }

    @RequestMapping(value = "/testTwoResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testTwoResult(){
        return "num计算结果：" + num  + "  volNum计算结果：" + volNum;
    }


    /**
     * 测试3
     */
    @RequestMapping(value = "/testThree",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized void testThree(){
        num++;
        volNum++;
        count.countDown();
    }

    @RequestMapping(value = "/testThreeResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testThreeResult(){
        return "同步方法测试---num计算结果：" + num  + "  volNum计算结果：" + volNum;
    }


    /**
     * 测试4
     */
    @RequestMapping(value = "/testFour",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized void testFour(){
        long timestamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
        try{
            //模拟code递增自增长
            num++;
            volNum++;
            long endstamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
            //模拟数据库、文件操作时间
            while(endstamp - timestamp < 1){
                endstamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常：" + e.getMessage());
        }finally {
            count.countDown();
        }
    }

    @RequestMapping(value = "/testFourResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testFourResult(){
        return "同步方法测试---num计算结果：" + num  + "  volNum计算结果：" + volNum;
    }

    /**
     * 测试5
     */
    @RequestMapping(value = "/testFive",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testFive(){
        long timestamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
        try{
            //模拟code递增自增长
            atomic.addAndGet(1);
            long endstamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
            //模拟数据库、文件操作时间
            while(endstamp - timestamp < 1){
                endstamp = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常：" + e.getMessage());
        }finally {
            count.countDown();
        }

    }

    @RequestMapping(value = "/testFiveResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testFiveResult(){
        return "AtomicInteger---atomic计算结果：" + atomic;
    }
}
