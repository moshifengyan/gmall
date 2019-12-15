package com.daitian.item.controller;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.PmsProductSaleAttr;
import com.daitian.bean.PmsSkuInfo;
import com.daitian.bean.PmsSkuSaleAttrValue;
import com.daitian.item.feign.ManagerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 代天 on 2019/12/1.
 */
@Controller
@CrossOrigin
public class ItemController {

    @Autowired
    ManagerFeignClient managerFeignClient;
    @RequestMapping("{skuId}.html")
    public String getSkuInfo(@PathVariable(name = "skuId")String skuId, ModelMap modelMap){
        //sku信息
        PmsSkuInfo pmsSkuInfo = managerFeignClient.getSkuById(skuId);
        modelMap.put("skuInfo",pmsSkuInfo);

        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = managerFeignClient.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        // 查询当前sku的spu的其他sku的集合的hash表
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = managerFeignClient.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";// "239|245"
            }
            skuSaleAttrHash.put(k,v);
        }

        // 将sku的销售属性hash表放到页面
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        return "item";
    }
}
