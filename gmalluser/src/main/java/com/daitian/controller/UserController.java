package com.daitian.controller;


import com.daitian.bean.UmsMember;
import com.daitian.bean.UmsMemberReceiveAddress;
import com.daitian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    @RequestMapping("getReceiveAddressById")
    @ResponseBody
    public UmsMemberReceiveAddress getReceiveAddressById(@RequestParam(name = "receiveAddressId") String receiveAddressId){
        return userService.getReceiveAddressById(receiveAddressId);
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("isMemeber")
    @ResponseBody
    public UmsMember isMember(UmsMember umsMember){
        return userService.isMemeber(umsMember);
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }



}
