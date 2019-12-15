package com.daitian.service.impl;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.OmsCartItem;
import com.daitian.bean.OmsOrder;
import com.daitian.bean.OmsOrderItem;
import com.daitian.mapper.OmsOrderItemMapper;
import com.daitian.mapper.OmsOrderMapper;
import com.daitian.service.OrderService;
import com.daitian.util.ActiveMQPoolUtil;
import com.daitian.util.RedisPoolUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import javax.jms.Queue;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisPoolUtil redisPoolUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    ActiveMQPoolUtil activeMQPoolUtil;

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = null ;

        try {
            jedis = redisPoolUtil.getJedis();
            String tradeKey = "user:" + memberId + ":tradeCode";


            //String tradeCodeFromCache = jedis.get(tradeKey);// 使用lua脚本在发现key的同时将key删除，防止并发订单攻击
            //对比防重删令牌
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval = (Long) jedis.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));

            if (eval!=null&&eval!=0) {
                jedis.del(tradeKey);
                return "success";
            } else {
                return "fail";
            }
        }finally {
            jedis.close();
        }

    }

    @Override
    public String genTradeCode(String memberId) {

        Jedis jedis = redisPoolUtil.getJedis();

        String tradeKey = "user:"+memberId+":tradeCode";

        String tradeCode = UUID.randomUUID().toString();

        jedis.setex(tradeKey,60*15,tradeCode);

        jedis.close();

        return tradeCode;
    }

    @Override
    public void saveOrder(OmsOrder omsOrder) {

        // 保存订单表
        omsOrderMapper.insertSelective(omsOrder);
        String orderId = omsOrder.getId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
            // 删除购物车数据
            // cartService.delCart();
        }
    }

    @Override
    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outTradeNo);
        OmsOrder omsOrder1 = omsOrderMapper.selectOne(omsOrder);

        return omsOrder1;
    }

    @Override
    public void updateOrder(String out_trade_no) throws JMSException {
        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("orderSn",out_trade_no);

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(out_trade_no);
        omsOrder.setStatus("1");//已支付待发货
        //更改订单状态
        omsOrderMapper.updateByExampleSelective(omsOrder,example);

        //创建消息队列更改库存
        ConnectionFactory connectionFactory = activeMQPoolUtil.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
        Queue ware_queue = session.createQueue("WARE_QUEUE");
        try{
            MessageProducer producer = session.createProducer(ware_queue);

            //创建消息体
            TextMessage textMessage = new ActiveMQTextMessage();
            OmsOrder wareOmsOrder = omsOrderMapper.selectOne(omsOrder);
            String wareOmsOrderId = wareOmsOrder.getId();
            OmsOrderItem omsOrderItem = new OmsOrderItem();
            omsOrderItem.setOrderId(wareOmsOrderId);
            List<OmsOrderItem> omsOrderItemList = omsOrderItemMapper.select(omsOrderItem);
            wareOmsOrder.setOmsOrderItems(omsOrderItemList);
            textMessage.setText(JSON.toJSONString(wareOmsOrder));
            producer.send(textMessage);
            session.commit();
        }catch (Exception ex){
            System.out.println("WARE_QUEUE发生异常，已回滚=======================================");
            session.rollback();
        } finally {
            session.close();
            connection.close();
        }



    }

    @Override
    public void cartItemDel(OmsCartItem omsCartItem) throws JMSException {

        //创建消息队列删除购物车，订单提交后就删除购物车，无论是否支付完成
        ConnectionFactory connectionFactory = activeMQPoolUtil.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
        try{
            Queue cart_queue = session.createQueue("CART_QUEUE");
            MessageProducer producer = session.createProducer(cart_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();
            Map<String,String> delCartItemMap = new HashMap<>();
            delCartItemMap.put("memberId",omsCartItem.getMemberId());
            delCartItemMap.put("skuId",omsCartItem.getProductSkuId());
            mapMessage.setObject("cartItem",delCartItemMap);
            producer.send(mapMessage);

            session.commit();
        }finally {
            session.close();
            connection.close();
        }

    }
}
