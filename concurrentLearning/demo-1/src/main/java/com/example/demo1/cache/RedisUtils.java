/**
 * @projectName jcgzglpt-commons
 * @package com.thunisoft.commons.utils
 * @className com.thunisoft.commons.utils.RedisUtils
 * @copyright Copyright 2018 Thuisoft, Inc. All rights reserved.
 */
package com.example.demo1.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Component;

/**
 * RedisUtils
 * 
 * @description redis工具类
 * @author Hewencai
 * @date 2018年4月18日 上午11:10:20
 * @version 1.0
 */
@Component
public class RedisUtils {

    /**
     * 注入redisTemplate
     */
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    /**
     * @param key
     * @return
     */
    public Integer generate(String key) {
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        Integer res = redisAtomicInteger.incrementAndGet();
        redisAtomicInteger.expire(Integer.MAX_VALUE, TimeUnit.DAYS);
        return res;
    }
}
