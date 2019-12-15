package com.daitian.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@FeignClient(name = "gmallmanager")
public interface ManagerFeignClientForOrder {

    @RequestMapping("checkPrice")
    @ResponseBody
    boolean checkPrice(@RequestParam(name = "productSkuId") String productSkuId, @RequestParam(name = "productPrice") BigDecimal productPrice);
}
