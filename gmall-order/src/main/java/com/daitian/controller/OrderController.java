package com.daitian.controller;

import com.alibaba.fastjson.JSON;
import com.daitian.annotations.LoginRequired;
import com.daitian.bean.OmsCartItem;
import com.daitian.bean.OmsOrder;
import com.daitian.bean.OmsOrderItem;
import com.daitian.bean.UmsMemberReceiveAddress;
import com.daitian.feign.CartFiegnClientForOrder;
import com.daitian.feign.ManagerFeignClientForOrder;
import com.daitian.feign.UmsFeignClientForOrder;
import com.daitian.feign.WareFeignClientForOrder;
import com.daitian.service.CartService;
import com.daitian.service.OrderService;
import com.daitian.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.suggest.fst.FSTCompletionLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    UmsFeignClientForOrder umsFeignClientForOrder;

    @Autowired
    CartFiegnClientForOrder cartFiegnClientForOrder;

    @Autowired
    ManagerFeignClientForOrder managerFeignClientForOrder;

    @Autowired
    WareFeignClientForOrder wareFeignClientForOrder;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @RequestMapping("getOrderByOutTradeNo")
    public OmsOrder getOrderByOutTradeNo(@RequestParam(name="outTradeNo") String outTradeNo){
        return orderService.getOrderByOutTradeNo(outTradeNo);
    }

    @RequestMapping("submitOrder")
    @LoginRequired(need = true)
    @Transactional
    public ModelAndView submitOrder(String receiveAddressId, BigDecimal totalAmount, String tradeCode, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) throws JMSException {


        String memberId = (String) request.getAttribute("memberId");

        // 检查交易码，防止订单重复提交
        String success = orderService.checkTradeCode(memberId, tradeCode);

        if (success.equals("success")) {
            List<OmsOrderItem> omsOrderItems = new ArrayList<>();
            // 订单对象
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setAutoConfirmDay(7);
            omsOrder.setCreateTime(new Date());
            omsOrder.setDiscountAmount(null);
            //omsOrder.setFreightAmount(); 运费，支付后，在生成物流信息时
            omsOrder.setMemberId(memberId);
//            omsOrder.setMemberUsername(nickname);
            omsOrder.setNote("快点发货");
            String outTradeNo = "gmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + sdf.format(new Date());// 将时间字符串拼接到外部订单号

            omsOrder.setOrderSn(outTradeNo);//外部订单号
            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(1);
            UmsMemberReceiveAddress umsMemberReceiveAddress = umsFeignClientForOrder.getReceiveAddressById(receiveAddressId);
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            // 当前日期加一天，一天后配送
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            Date time = c.getTime();
            omsOrder.setReceiveTime(time);
            omsOrder.setSourceType(0);
            omsOrder.setStatus("0");
            omsOrder.setOrderType(0);
            omsOrder.setTotalAmount(totalAmount);

            // 根据用户id获得要购买的商品列表(购物车)，和总价格
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);

            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1")) {
                    // 获得订单详情列表
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    // 检价
                    boolean b = managerFeignClientForOrder.checkPrice(omsCartItem.getProductSkuId(),omsCartItem.getPrice());
                    if (b == false) {
                        ModelAndView mv = new ModelAndView("tradeFail");
                        mv.addObject("errMsg","商品价格变动");
                        return mv;
                    }

                    // 验库存,远程调用库存系统
                    HashMap<String,String> stockMap = new HashMap<>();
                    stockMap.put("num",String.valueOf(omsCartItem.getQuantity()));
                    stockMap.put("skuId",omsCartItem.getProductSkuId());
                    if(!wareFeignClientForOrder.hasStock(stockMap)){//如果库存不够
                        ModelAndView mv = new ModelAndView("tradeFail");
                        String errMsg = new String("skuId:"+stockMap.get("skuId")+",skuName:"+stockMap.get("num")+"库存不足");
                        mv.addObject("errMsg",errMsg);
                        return mv;
                    }
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());

                    omsOrderItem.setOrderSn(outTradeNo);// 外部订单号，用来和其他系统进行交互，防止重复
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setProductSkuCode("111111111111");
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");// 在仓库中的skuId

                    omsOrderItems.add(omsOrderItem);

                    // 创建消息队列删除购物车
                    orderService.cartItemDel(omsCartItem);
                }
            }
            omsOrder.setOmsOrderItems(omsOrderItems);

            // 将订单和订单详情写入数据库
            orderService.saveOrder(omsOrder);


            // 重定向到支付系统
            ModelAndView mv = new ModelAndView("redirect:http://localhost:8008/index");
            Object id = omsOrder.getId();
            mv.addObject("outTradeNo",outTradeNo);
            mv.addObject("totalAmount",totalAmount);
            mv.addObject("orderId",id);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("tradeFail");
            mv.addObject("errMsg","订单重复提交");
            return mv;
        }
    }

    @RequestMapping("toTrade")
    @LoginRequired(need = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        // 收件人地址列表
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsFeignClientForOrder.getReceiveAddressByMemberId(memberId);

        // 将购物车集合转化为页面计算清单集合
        List<OmsCartItem> omsCartItems = cartFiegnClientForOrder.cartListForOrder(memberId);

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();

        //从cookie中读取购物车数据
        String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
        if (StringUtils.isNotBlank(cartListCookie)) {//如果cookie不为空
            List<OmsCartItem> cookieList = JSON.parseArray(cartListCookie, OmsCartItem.class);
            //将cookie中购物车数据写入数据库或者缓存
            if(StringUtils.isNotBlank(memberId)){
                for (OmsCartItem o:cookieList) {
                    o.setMemberId(memberId);
                    cartService.addCart(o);
                }
            }
            omsCartItems.addAll(cookieList);//返回给前台
        }

        for (OmsCartItem omsCartItem : omsCartItems) {
            // 每循环一个购物车对象，就封装一个商品的详情到OmsOrderItem
            if (omsCartItem.getIsChecked().equals("1")) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }

        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("userAddressList", umsMemberReceiveAddresses);
        modelMap.put("totalAmount", getTotalAmount(omsCartItems));
        modelMap.put("memberId",memberId);

        // 生成交易码，为了在提交订单时做交易码的校验
        String tradeCode = orderService.genTradeCode(memberId);
        modelMap.put("tradeCode", tradeCode);
        return "trade";

    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if (omsCartItem.getIsChecked().equals("1")) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

}
