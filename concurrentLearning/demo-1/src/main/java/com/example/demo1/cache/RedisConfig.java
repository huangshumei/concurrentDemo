/**
 * @projectName jcgzglpt-uim
 * @package com.thunisoft.uim.cache
 * @className com.thunisoft.uim.cache.RedisConfig
 * @copyright Copyright 2018 Thuisoft, Inc. All rights reserved.
 */
package com.example.demo1.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfig
 * 
 * @description RedisConfig
 * @author Hewencai
 * @date 2018年4月18日 下午2:00:44
 * @version 1.0
 */
@EnableAutoConfiguration
@Configuration
@EnableCaching
public class RedisConfig {
    /** 服务器地址 */
    @Value("${spring.redis.host}")
    private String host;
    /** 服务器连接端口 */
    @Value("${spring.redis.port}")
    private int port;
    /** 服务器连接密码 */
    @Value("${spring.redis.password}")
    private String password;
    /** 数据库索引 */
    @Value("${spring.redis.database}")
    private Integer database;
    /** 连接池最大连接数（使用负值表示没有限制） */
    @Value("${spring.redis.pool.max-active}")
    private Integer maxActive;
    /** 连接池最大阻塞等待时间（使用负值表示没有限制） */
    @Value("${spring.redis.pool.max-wait}")
    private Long maxWait;
    /** 连接池中的最大空闲连接 */
    @Value("${spring.redis.pool.max-idle}")
    private Integer maxIdle;
    /** 连接池中的最小空闲连接 */
    @Value("${spring.redis.pool.min-idle}")
    private Integer minIdle;
    /** 在获取连接的时候检查有效性 */
    @Value("${spring.redis.pool.testOnBorrow}")
    private boolean testOnBorrow;
    /** 在返回连接的时候检查有效性 */
    @Value("${spring.redis.pool.testOnReturn}")
    private boolean testOnReturn;

    /**
     * 
     * RedisConfig
     * 
     * @description 获取连接
     * @return
     * @author Hewencai
     * @date 2018年5月3日 下午6:50:13
     * @version 1.0
     */
    @Bean
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        RedisStandaloneConfiguration config = jedisConnectionFactory.getStandaloneConfiguration();
        config.setHostName(this.host);
        config.setPort(this.port);
        config.setPassword(RedisPassword.of(this.password));
        config.setDatabase(this.database);
        GenericObjectPoolConfig genericObjectPoolConfig
            = jedisConnectionFactory.getClientConfiguration().getPoolConfig().get();
        genericObjectPoolConfig.setMaxTotal(this.maxActive);
        genericObjectPoolConfig.setMaxIdle(this.maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(this.maxWait);
        genericObjectPoolConfig.setMinIdle(this.minIdle);
        genericObjectPoolConfig.setTestOnBorrow(this.testOnBorrow);
        genericObjectPoolConfig.setTestOnReturn(this.testOnReturn);
        return jedisConnectionFactory;
    }

    /**
     * 
     * RedisConfig
     * 
     * @description 注入redisTemplate
     * @return
     * @author Hewencai
     * @date 2018年5月3日 下午6:49:59
     * @version 1.0
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new StringRedisTemplate(getConnectionFactory());
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

}
