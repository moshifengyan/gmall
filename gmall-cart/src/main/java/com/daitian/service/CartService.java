package com.daitian.service;

import com.daitian.bean.OmsCartItem;

import javax.jms.JMSException;
import java.util.List;

public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);
    void addCart(OmsCartItem omsCartItem);
    void updateCart(OmsCartItem omsCartItem);
    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String memeberId);

    void checkCart(OmsCartItem omsCartItem);

}
