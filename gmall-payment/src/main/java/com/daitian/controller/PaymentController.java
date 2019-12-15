package com.daitian.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.daitian.annotations.LoginRequired;
import com.daitian.bean.OmsOrder;
import com.daitian.bean.PaymentInfo;
import com.daitian.config.AlipayConfig;
import com.daitian.feign.OrderFiegnClientForPayment;
import com.daitian.service.PaymentService;
import com.daitian.util.ActiveMQPoolUtil;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderFiegnClientForPayment orderFiegnClientForPayment;

    @Autowired
    ActiveMQPoolUtil activeMQPoolUtil;

    @RequestMapping("index")
    @LoginRequired(need = true)
    public String index(String orderId,String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap){
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        modelMap.put("outTradeNo",outTradeNo);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("orderId",orderId);
        return "index";
    }

    @RequestMapping("alipay/submit")
    @LoginRequired(need=true)
    @ResponseBody
    public String aliPay(String orderId, String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap) throws AlipayApiException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        // 回调函数
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);


        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outTradeNo);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        map.put("total_amount",0.01);
        map.put("subject","谷粒商城商品");

        String param = JSON.toJSONString(map);
        alipayRequest.setBizContent(param);
        String form = alipayClient.pageExecute(alipayRequest).getBody();//返回支付宝扫码页面
        System.out.println(form);

        //生成并且保存用户的支付信息
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderId);
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject("谷粒商城商品一件");
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);
        return form;
    }

    //支付成功后，支付宝会回调该函数
    @RequestMapping("alipay/callback/return")
    @LoginRequired(need = true)
    public String aliPayCallBackReturn(HttpServletRequest request, ModelMap modelMap) throws JMSException {

        // 回调请求中获取支付宝参数
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");//// 支付宝的交易凭证号
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();//回调请求字符串
        if(StringUtils.isNoneBlank(sign)){
            PaymentInfo paymentInfo = new PaymentInfo();
            /**********需要更新的字段********************/
            paymentInfo.setAlipayTradeNo(trade_no);
            paymentInfo.setCallbackContent(call_back_content);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setCallbackTime(new Date());//回调时间
            /*******************************************/
            paymentInfo.setOrderSn(out_trade_no);

            //更新paymentInfo信息，其中包括向消息队列中生产消息，供订单服务消费，库存服务消费，更新订单状态，库存
            paymentService.updatePaymentInfo(paymentInfo);
        }
        return "finish";
    }

}
