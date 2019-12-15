package com.daitian.conf;

import com.daitian.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
//@ConfigurationProperties(prefix = "myredis")
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.database}")
    private int database;

    @Bean
    public RedisPoolUtil getRedisConnection(){
        if(host.equals("disabled")){
            return null;
        }
        RedisPoolUtil redisPoolUtil = new RedisPoolUtil();
        redisPoolUtil.createPool(host,port);
        return redisPoolUtil;
    }
}
