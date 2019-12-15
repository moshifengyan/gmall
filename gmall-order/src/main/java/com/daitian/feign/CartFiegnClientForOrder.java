package com.daitian.feign;

import com.daitian.bean.OmsCartItem;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import java.util.List;

@FeignClient(name = "gmallcart")
public interface CartFiegnClientForOrder {
    @RequestMapping("cartListForOrder")
    List<OmsCartItem> cartListForOrder(@RequestParam(name = "memberId") String memberId);
}
