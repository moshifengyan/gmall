package com.daitian.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisPoolUtil {
    private JedisPool jedisPool;
    public void createPool(String host,int port){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(30);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(10*1000);
        poolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(poolConfig,host,port,20*1000);
    }
    public Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
    public void closeJedis(Jedis jedis){
        jedis.close();
    }
}
