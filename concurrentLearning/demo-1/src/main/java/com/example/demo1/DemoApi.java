package com.example.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DemoApi {

    private static Integer num = 0;

    private static volatile Integer volNum = 0;

    public static CountDownLatch count;

    public static AtomicInteger atomic = new AtomicInteger(0);

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;



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
        try{
            //模拟code递增自增长
            num++;
            volNum++;
            long timestamp = System.currentTimeMillis();
            long endstamp = System.currentTimeMillis();
            //模拟数据库、文件操作时间
            while(endstamp - timestamp < 100){
                endstamp = System.currentTimeMillis();
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
        long timestamp = System.currentTimeMillis();
        try{
            //模拟code递增自增长
            atomic.addAndGet(1);
            long endstamp = System.currentTimeMillis();
            //模拟数据库、文件操作时间
            while(endstamp - timestamp < 10){
                endstamp = System.currentTimeMillis();
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


    /**
     * 测试6
     */
    @RequestMapping(value = "/testSix",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int testSix(){
        int redisNum = 0;
        try{
            //模拟code递增自增长
            RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger("demo_key", redisTemplate.getConnectionFactory());
            redisNum = redisAtomicInteger.incrementAndGet();
            redisAtomicInteger.expire(Integer.MAX_VALUE, TimeUnit.DAYS);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常：" + e.getMessage());
        }finally {
            count.countDown();
        }
        return redisNum;
    }

    @RequestMapping(value = "/testSixResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testSixResult(){
        String result = redisTemplate.execute((RedisCallback<String>)(connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value =  connection.get(serializer.serialize("demo_key"));
            return serializer.deserialize(value);
        });
        return "redis---num计算结果：" + result;
    }
}
