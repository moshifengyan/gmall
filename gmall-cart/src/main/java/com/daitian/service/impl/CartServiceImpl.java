package com.daitian.service.impl;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.OmsCartItem;
import com.daitian.mapper.OmsCartItemMapper;
import com.daitian.service.CartService;
import com.daitian.util.ActiveMQPoolUtil;
import com.daitian.util.RedisPoolUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    RedisPoolUtil redisPoolUtil;

    @Autowired
    ActiveMQPoolUtil activeMQPoolUtil;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;

    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        System.out.println(omsCartItem.getQuantity());
        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            omsCartItemMapper.insertSelective(omsCartItem);//避免添加空值
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {

        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id",omsCartItemFromDb.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb,e);

    }

    @Override
    public void flushCartCache(String memeberId) {
        Jedis jedis = redisPoolUtil.getJedis();
        try {
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memeberId);
            List<OmsCartItem> omsCartItemList = omsCartItemMapper.select(omsCartItem);
            Map<String,String> map = new HashMap<>();
            for (OmsCartItem o:omsCartItemList) {
                o.setTotalPrice(o.getPrice().multiply(o.getQuantity()));
                map.put(o.getProductSkuId(),JSON.toJSONString(o));
            }
            String key = "user"+memeberId+"cart";
            jedis.del(key);
            if(map.size()!=0){
                jedis.hmset(key,map);
            }
        }finally {
            jedis.close();
        }
    }

    @Override
    public List<OmsCartItem> cartList(String memeberId) {
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        Jedis jedis = redisPoolUtil.getJedis();
        Map<String,String> map = new HashMap<>();
        try {
            String key = "user"+memeberId+"cart";
            List<String> cartList = jedis.hvals(key);
            if(cartList!=null){//缓存中有数据
                for (String cart:cartList) {
                    omsCartItemList.add(JSON.parseObject(cart,OmsCartItem.class));
                }
            }else{//缓存中没有数据
                //从DB中查询
                OmsCartItem omsCartItem = new OmsCartItem();
                omsCartItem.setMemberId(memeberId);
                omsCartItemList = omsCartItemMapper.select(omsCartItem);

                //将查询结果刷入缓存
                for (OmsCartItem o:omsCartItemList) {
                    o.setTotalPrice(o.getPrice().multiply(o.getQuantity()));
                    map.put(o.getProductId(),JSON.toJSONString(o));
                }
                jedis.hmset(key,map);
            }
            return omsCartItemList;
        }finally {
            jedis.close();
        }

    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem,e);
        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }


}
