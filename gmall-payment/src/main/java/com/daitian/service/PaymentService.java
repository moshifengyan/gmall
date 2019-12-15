package com.daitian.service;

import com.daitian.bean.PaymentInfo;

import javax.jms.JMSException;
import java.util.Map;

public interface PaymentService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    Map<String, Object> checkAlipayPayment(String out_trade_no);

    void updatePaymentInfo(PaymentInfo paymentInfo) throws JMSException;
}
