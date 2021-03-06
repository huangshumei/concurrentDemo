package com.example.demo1;

import com.example.demo1.db.TDemo;
import com.example.demo1.db.TDemoRepository;
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


    @Autowired
    private TDemoRepository repository;


    /**
     * 测试1--非线程安全
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
     * 测试2--volatile关键字
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
     * 测试3-线程同步
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
     * 测试4-操作限时
     */
    @RequestMapping(value = "/testFour",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testFour(){
        try{
            repository.generatorBm();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常：" + e.getMessage());
        }finally {
            count.countDown();
        }
    }

    @RequestMapping(value = "/testFourResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String testFourResult(){
        int value = 0;
        TDemo demo = repository.findTDemoById("demo");
        if(demo != null){
            value = demo.getValue();
        }
        return "同步方法测试---num计算结果：" + value;
    }

    /**
     * 测试5-Atomic方法
     */
    @RequestMapping(value = "/testFive",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void testFive(){
        try{
            //模拟code递增自增长
            atomic.addAndGet(1);
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
     * 测试6-redis
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
