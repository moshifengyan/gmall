package com.daitian.mq;

import com.daitian.bean.OmsCartItem;
import com.daitian.mapper.OmsCartItemMapper;
import com.daitian.service.CartService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.HashMap;

@Component
public class CartMQListener {
    @Autowired
    CartService cartService;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @JmsListener(destination = "CART_QUEUE",containerFactory = "jmsQueueListener")
    public void cartListener(MapMessage mapMessage) throws JMSException {
        HashMap<String,String> delOmsCartItemMaop = (HashMap<String, String>) mapMessage.getObject("cartItem");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(delOmsCartItemMaop.get("memberId"));
        omsCartItem.setProductSkuId(delOmsCartItemMaop.get("skuId"));
        omsCartItemMapper.delete(omsCartItem);
        cartService.flushCartCache(delOmsCartItemMaop.get("memberId"));

    }
}
