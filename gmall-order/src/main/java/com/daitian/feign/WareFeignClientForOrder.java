package com.daitian.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient("gmallware")
public interface WareFeignClientForOrder {
    @RequestMapping("hasStock")
    @ResponseBody
    boolean hasStock(@RequestParam Map<String,String> hashMap);
}
