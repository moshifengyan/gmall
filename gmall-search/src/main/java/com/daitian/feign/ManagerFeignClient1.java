package com.daitian.feign;


import com.daitian.bean.PmsBaseAttrInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "gmallmanager")
public interface ManagerFeignClient1 {
    @PostMapping("getAttrValueListByValueIds")
    List<PmsBaseAttrInfo> getAttrValueListByValueIds(@RequestBody Set<String> valueIdSet);
}
