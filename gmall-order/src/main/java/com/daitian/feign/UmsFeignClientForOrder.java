package com.daitian.feign;

import com.daitian.bean.UmsMemberReceiveAddress;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "gmalluser")
public interface UmsFeignClientForOrder {

    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(@RequestParam(name = "memberId") String memeberId);

    @RequestMapping("getReceiveAddressById")
    @ResponseBody
    UmsMemberReceiveAddress getReceiveAddressById(@RequestParam(name = "receiveAddressId")String receiveAddressId);
}
