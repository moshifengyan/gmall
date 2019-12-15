package com.daitian.service.impl;

import com.daitian.service.SeckillSevice;
import com.daitian.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class SeckillServiceImpl implements SeckillSevice {

    @Autowired
    RedisPoolUtil redisPoolUtil;

    @Override
    public String seckill(){
        Jedis jedis = redisPoolUtil.getJedis();
        try {
            jedis.watch("product");
            int num = Integer.valueOf(jedis.get("product"));
            if(num>0){
                Transaction multi = jedis.multi();
                multi.incrBy("product",-1);
                List<Object> exec = multi.exec();
                if(exec!=null&&exec.size()>0){
                    System.out.println(Thread.currentThread().getName()+"用户购买了一个手机，还有"+num+"个");
                }else{
                    System.out.println(Thread.currentThread().getName()+"抢购失败");
                }
            }
            else{
                System.out.println("商品已经出售完，抢购失败");
            }
            return "1";
        }finally {
            redisPoolUtil.closeJedis(jedis);
        }
    }

    @Override
    public String seckillLock(){
        Jedis jedis = redisPoolUtil.getJedis();
        try{
            String member = Thread.currentThread().getName()+ RandomUtils.nextLong();
            int num = Integer.valueOf(jedis.get("product"));
            if(num>0){
                String success = jedis.set("lock",member,"nx","px",60*60*24);
                if(StringUtils.isNotBlank(success)&&success.equals("OK")){//成功获取锁
                    jedis.incrBy("product",-1);//削减库存
                    System.out.println("用户"+member+"购买手机成功，还剩"+Integer.valueOf(jedis.get("product")));
                    String script = "if redis.call('get', 'lock') == '"+member+"'\n" +
                            "    then\n" +
                            "        return redis.call('del', 'lock')\n" +
                            "    else\n" +
                            "        return 0\n" +
                            "end";
                    jedis.eval(script);
                }else{//获取锁失败
//                    System.out.println("用户"+member+"购买手机失败");
                }
            }else{
                System.out.println("商品已经售完");
            }
            return "1";
        }finally {
            redisPoolUtil.closeJedis(jedis);
        }
    }

}
