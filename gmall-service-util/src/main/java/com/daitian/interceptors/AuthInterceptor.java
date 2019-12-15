package com.daitian.interceptors;

import com.daitian.annotations.LoginRequired;
import com.daitian.util.CookieUtil;
import com.daitian.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//创建拦截器
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println(handler);
//        System.out.println(handler.toString());
//        System.out.println(request.getRequestURL());
        HashMap<String,Object> map = new HashMap<>();
        HandlerMethod hm = (HandlerMethod)handler;//将controller对象强制转换为HandlerMethod对象，以获取注解
        LoginRequired loginAnnotation = hm.getMethodAnnotation(LoginRequired.class);



        String token = "";
        token = CookieUtil.getCookieValue(request,"token",true);

        //获取请求的ip
        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }

        if (loginAnnotation != null){//如果加了注解
            boolean need = loginAnnotation.need();
            if(need){//如果需要登陆
                if(StringUtils.isBlank(token)){//如果没有token
                    response.sendRedirect("http://localhost:8006/index?ReturnUrl="+request.getRequestURL());
                    return false;
                }else{
                    if(JwtUtil.verifyToken(token,"mygmall",ip)){//token验证成功
                        //更新cookie的过期时间
                        String memberId = (String)JwtUtil.decode(token,"mygmall",ip).get("memberId");
                        map.put("memberId",memberId);
                        token = JwtUtil.encode("mygmall",(Map<String, Object>) map,ip);
                        CookieUtil.setCookie(request,response,"token",token,60*60*72,true);
                        request.setAttribute("memberId",memberId);
                        return true;
                    }else{//token验证失败或者过期,则重定向到重新登陆
                        response.sendRedirect("http://localhost:8006/index?ReturnUrl="+request.getRequestURL());
                        return false;
                    }
                }

            }else{//不需要登陆
                if(StringUtils.isNotBlank(token)) {//如果有token
                    if (JwtUtil.verifyToken(token, "mygmall", ip)) {//token验证成功
                        //更新cookie的过期时间
                        String memberId = (String) JwtUtil.decode(token,"mygmall",ip).get("memberId");
                        map.put("memberId",memberId);
                        token = JwtUtil.encode("mygmall",(Map<String, Object>) map,ip);
                        CookieUtil.setCookie(request, response, "token", token, 60 * 60 * 72, true);
                        request.setAttribute("memberId", memberId);
                    }
                }
                return true;
            }
        }else{//如果没有加注解的话,不需要登陆
            if(StringUtils.isNotBlank(token)) {//如果有token
                if (JwtUtil.verifyToken(token, "mygmall", ip)) {//token验证成功
                    //更新cookie的过期时间
                    String memberId = (String) JwtUtil.decode(token,"mygmall",ip).get("memberId");
                    map.put("memberId",memberId);
                    token = JwtUtil.encode("mygmall",(Map<String, Object>) map,ip);
                    CookieUtil.setCookie(request, response, "token", token, 60 * 60 * 72, true);
                    request.setAttribute("memberId", memberId);
                }
            }
            return true;
        }
    }
}
