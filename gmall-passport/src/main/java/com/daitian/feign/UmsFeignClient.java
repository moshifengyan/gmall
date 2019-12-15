package com.daitian.feign;

import com.daitian.bean.UmsMember;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "gmalluser")
public interface UmsFeignClient {
    @RequestMapping("isMemeber")
    UmsMember isMember(UmsMember umsMember);
}
