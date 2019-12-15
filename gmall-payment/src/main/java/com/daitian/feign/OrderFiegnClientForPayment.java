package com.daitian.feign;

import com.daitian.bean.OmsCartItem;
import com.daitian.bean.OmsOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gmallorder")
public interface OrderFiegnClientForPayment {
    @RequestMapping("getOrderByOutTradeNo")
    public OmsOrder getOrderByOutTradeNo(@RequestParam(name="outTradeNo") String outTradeNo);
}

