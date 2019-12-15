package com.daitian.controller;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.UmsMember;
import com.daitian.feign.UmsFeignClient;
import com.daitian.util.CookieUtil;
import com.daitian.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassportController {

    @Autowired
    UmsFeignClient umsFeignClient;

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request, HttpServletResponse response){

        String token;
        UmsMember isMember = umsFeignClient.isMember(umsMember);

        if(isMember!=null){//如果db验证成功
            String memberId = String.valueOf(isMember.getId());
            //生成新的token，并且覆盖旧的token
            Map<String ,Object> userMap = new HashMap<>();
            userMap.put("memberId",memberId);

            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }

            // 按照设计的算法对参数进行加密后，生成token
            token = JwtUtil.encode("mygmall", userMap, ip);
            CookieUtil.setCookie(request,response,"token",token,60*60*72,true);
            request.setAttribute("memberId",memberId);

        }else{//如果db验证失败
            token = "fail";
        }
        return token;
    }
    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap){
        String originUrl = "http://localhost:8004/index";
        if(StringUtils.isNotBlank(ReturnUrl)){
            originUrl = ReturnUrl;
        }
        modelMap.put("ReturnUrl",originUrl);
        return "index";
    }
}
