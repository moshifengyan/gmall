package com.daitian.controller;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.OmsCartItem;
import com.daitian.bean.PmsSkuInfo;
import com.daitian.feign.ManagerFeginClient2;
import com.daitian.service.CartService;
import com.daitian.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class CartController {

    @Autowired
    ManagerFeginClient2 managerFeginClient2;

    @Autowired
    CartService cartService;

    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response){
        //购物车
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        //待添加商品
        OmsCartItem omsCartItem = new OmsCartItem();
        PmsSkuInfo pmsSkuInfo = managerFeginClient2.getSkuById(skuId);
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        omsCartItem.setIsChecked("0");
        omsCartItem.setTotalPrice(omsCartItem.getPrice());

        //尝试获取用户id，判断用户时候登陆
        String memberId = (String)request.getAttribute("memberId");
//        String nickname = (String)request.getAttribute("nickname");

        //判断cookie是否存在
        String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
        if(StringUtils.isBlank(memberId)){//用户没有登陆，存入cookie
            if(StringUtils.isNotBlank(cartListCookie)){//如果cookie存在
                //旧的购物车
                omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
                if(if_cart_exist(omsCartItemList,omsCartItem)){//如果购物车中已存在，增加数量
                    //循环找出该物品，修改数量和总价
                    for (OmsCartItem o:omsCartItemList) {
                        if(o.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            o.setQuantity(new BigDecimal(o.getQuantity().intValue()+quantity));
                            o.setTotalPrice(new BigDecimal(o.getQuantity().intValue()*o.getPrice().intValue()));
                            break;
                        }
                    }
                }else{//如果不存在，添加购物车
                    omsCartItemList.add(omsCartItem);
                }

            }else{
                omsCartItemList.add(omsCartItem);
            }
            String dd = JSON.toJSONString(omsCartItemList);
            //刷新cookie
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(omsCartItemList),60 * 60 * 72,true);
        }else{//用户已经登陆，更新db
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId,skuId);
            if(omsCartItemFromDb==null){//如果购物车中没有该商品
                omsCartItem.setMemberId(memberId);
//                omsCartItem.setMemberNickname(nickname);
                omsCartItem.setQuantity(new BigDecimal(quantity));
                omsCartItem.setTotalPrice(new BigDecimal(omsCartItem.getQuantity().intValue()*omsCartItem.getPrice().intValue()));
                cartService.addCart(omsCartItem);//添加商品
            }else{//如果购物车中有该商品
                omsCartItemFromDb.setQuantity(new BigDecimal(omsCartItemFromDb.getQuantity().intValue()+quantity));
                omsCartItemFromDb.setTotalPrice(new BigDecimal(omsCartItemFromDb.getQuantity().intValue()*omsCartItemFromDb.getPrice().intValue()));
                cartService.updateCart(omsCartItemFromDb);
            }
            //同步缓存
            cartService.flushCartCache(memberId);
        }
        return "redirect:/success.html";
    }

    @RequestMapping("checkCart")
    public String checkCart(String isChecked, String skuId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

        List<OmsCartItem> omsCartItemList;
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        OmsCartItem omsCartItem = new OmsCartItem();
        if(StringUtils.isBlank(memberId)){//如果用户没有登陆，修改cookie中数据
            omsCartItem.setProductSkuId(skuId);
            omsCartItem.setIsChecked(isChecked);
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItemList = JSON.parseArray(cartListCookie,OmsCartItem.class);
            //修改cookie中的选中状态
            for (OmsCartItem o:omsCartItemList) {
                if(o.getProductSkuId().equals(skuId)){
                    o.setIsChecked(isChecked);
                    break;
                }
            }
            //更新cookie
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(omsCartItemList),60 * 60 * 72,true);



        }else{//如果用户登陆，修改db以及缓存中数据
            // 调用服务，修改状态

            omsCartItem.setMemberId(memberId);
            omsCartItem.setProductSkuId(skuId);
            omsCartItem.setIsChecked(isChecked);

            cartService.checkCart(omsCartItem);

            // 将最新的数据从缓存中查出，渲染给内嵌页
            omsCartItemList = cartService.cartList(memberId);
        }

        modelMap.put("cartList",omsCartItemList);
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItemList);
        modelMap.put("totalAmount",totalAmount);
        return "cartListInner";
    }

    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {
        //购物车
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        String memeberId = (String)request.getAttribute("memberId") ;
        if (StringUtils.isNotBlank(memeberId)) {//如果用户登陆
            //从DB中读取数据与cookie的数据合并
            omsCartItemList.addAll(cartService.cartList(memeberId));

        }
        //从cookie中读取购物车数据
        String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
        if (StringUtils.isNotBlank(cartListCookie)) {//如果cookie不为空
            List<OmsCartItem> cookieList = JSON.parseArray(cartListCookie, OmsCartItem.class);
            //将cookie中购物车数据写入数据库或者缓存
            if(StringUtils.isNotBlank(memeberId)){
                for (OmsCartItem o:cookieList) {
                    for (OmsCartItem odb:omsCartItemList) {
                        if(odb.getProductSkuId().equals(o.getProductSkuId())){
                            //将cookie的数量合并到db
                            odb.setQuantity(new BigDecimal(odb.getQuantity().intValue()+o.getQuantity().intValue()));
                            cartService.updateCart(odb);
                        }
                    }
                }
            }
        }
        //清除cookie
        CookieUtil.deleteCookie(request, response,"cartListCookie");
        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        modelMap.put("cartList", omsCartItemList);
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItemList);
        modelMap.put("totalAmount",totalAmount);
        if(memeberId!="null")modelMap.put("userId",memeberId);
        return "cartList";
    }

    @RequestMapping("cartListForOrder")
    @ResponseBody
    public List<OmsCartItem> cartListForOrder(String memberId) {
        //购物车
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        if (StringUtils.isNotBlank(memberId)) {
            omsCartItemList = cartService.cartList(memberId);
        }

        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        return omsCartItemList;
    }


    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if(omsCartItem.getIsChecked().equals("1")){
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

    private boolean if_cart_exist(List<OmsCartItem> omsCartItemList,OmsCartItem omsCartItem){
        boolean result = false;
        for (OmsCartItem o:omsCartItemList) {
            if(o.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                return true;
            }
        }
        return result;
    }
}
