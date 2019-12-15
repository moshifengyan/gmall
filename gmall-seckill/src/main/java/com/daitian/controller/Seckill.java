package com.daitian.controller;

import com.daitian.service.SeckillSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class Seckill {

    @Autowired
    SeckillSevice seckillSevice;

    @RequestMapping("seckill")
    @ResponseBody
    public String seckill(){
//        return seckillSevice.seckill();
        return seckillSevice.seckillLock();
    }
}
