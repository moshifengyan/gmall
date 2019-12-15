package com.daitian.feign;

import com.daitian.bean.PmsSkuInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="gmallmanager")
public interface ManagerFeginClient2 {
    @RequestMapping("getSkuById")
    PmsSkuInfo getSkuById(@RequestParam(name = "skuId")String skuId);
}
