package com.daitian.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.daitian.bean.PaymentInfo;
import com.daitian.mapper.PaymentInfoMapper;
import com.daitian.service.PaymentService;
import com.daitian.util.ActiveMQPoolUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    ActiveMQPoolUtil activeMQPoolUtil;

    @Autowired
    AlipayClient alipayClient;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }


//    @Override
//    public void sendDelayPaymentResultCheckQueue(String outTradeNo,int count) {
//
//        Connection connection = null;
//        Session session = null;
//        try {
//            connection = activeMQUtil.getConnectionFactory().createConnection();
//            session = connection.createSession(true, Session.SESSION_TRANSACTED);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//
//        try{
//            Queue payhment_success_queue = session.createQueue("PAYMENT_CHECK_QUEUE");
//            MessageProducer producer = session.createProducer(payhment_success_queue);
//
//            //TextMessage textMessage=new ActiveMQTextMessage();//字符串文本
//
//            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构
//
//            mapMessage.setString("out_trade_no",outTradeNo);
//            mapMessage.setInt("count",count);
//
//            // 为消息加入延迟时间
//            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*60);
//
//            producer.send(mapMessage);
//
//            session.commit();
//        }catch (Exception ex){
//            // 消息回滚
//            try {
//                session.rollback();
//            } catch (JMSException e1) {
//                e1.printStackTrace();
//            }
//        }finally {
//            try {
//                connection.close();
//            } catch (JMSException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    @Override
    public Map<String, Object> checkAlipayPayment(String out_trade_no) {

        Map<String,Object> resultMap = new HashMap<>();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no",out_trade_no);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("有可能交易已创建，调用成功");
            resultMap.put("out_trade_no",response.getOutTradeNo());
            resultMap.put("trade_no",response.getTradeNo());
            resultMap.put("trade_status",response.getTradeStatus());
            resultMap.put("call_back_content",response.getMsg());
        } else {
            System.out.println("有可能交易未创建，调用失败");

        }

        return resultMap;
    }

    @Override
    public void updatePaymentInfo(PaymentInfo paymentInfo) throws JMSException {

        String orderSn = paymentInfo.getOrderSn();
        Example e =new Example(PaymentInfo.class);
        e.createCriteria().andEqualTo("orderSn",orderSn);
        paymentInfoMapper.updateByExampleSelective(paymentInfo,e);
        //创建activemq连接
        Connection connection = activeMQPoolUtil.getConnectionFactory().createConnection();
        Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
        try {
            Queue payment_success_queue = session.createQueue("PAYMENT_SUCCESS_QUEUE");
            MessageProducer producer = session.createProducer(payment_success_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("out_trade_no",paymentInfo.getOrderSn());

            producer.send(mapMessage);
            session.commit();

        }finally {
            try {
                //关闭连接
                session.close();
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}
