package com.daitian.mq;

import com.daitian.bean.OmsOrder;
import com.daitian.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderMQListener {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
    public void paymentListener(MapMessage mapMessage) throws JMSException {
        //从消息队列中获取消息
        String out_trade_no = mapMessage.getString("out_trade_no");

        //根据获取的外部订单号更新订单状态
        orderService.updateOrder(out_trade_no);

    }

}
