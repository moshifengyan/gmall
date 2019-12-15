package com.daitian;

import com.daitian.util.RedisPoolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallmanagerApplicationTests {
    @Autowired
    RedisPoolUtil redisPoolUtil;
    @Test
    public void test(){
        Jedis jedis =  redisPoolUtil.getJedis();
        System.out.println(jedis);
    }


}
