package com.daitian.service;

import com.daitian.bean.OmsCartItem;
import com.daitian.bean.OmsOrder;

import javax.jms.JMSException;

public interface OrderService {
    String checkTradeCode(String memberId, String tradeCode);

    String genTradeCode(String memberId);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(String out_trade_no) throws JMSException;

    void cartItemDel(OmsCartItem omsCartItem) throws JMSException;
}
