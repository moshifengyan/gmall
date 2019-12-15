package com.daitian.controller;

import com.daitian.bean.PmsSkuInfo;
import com.daitian.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 代天 on 2019/12/1.
 */
@RestController
@CrossOrigin
public class SkuController {

    @Autowired
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @Transactional
    public void saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        skuService.saveSkuInfo(pmsSkuInfo);
        skuService.updateElastic();
    }


    @RequestMapping("getSkuById")
    public PmsSkuInfo getSkuById(@RequestParam(name = "skuId")String skuId){
        return skuService.getSkuById(skuId);
    }

    @RequestMapping("checkPrice")
    public boolean checkPrice(String productSkuId, BigDecimal productPrice){
        return skuService.checkPrice(productSkuId,productPrice);
    }

    @RequestMapping("getSkuSaleAttrValueListBySpu")
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam(value = "productId")String productId){
        return skuService.getSkuSaleAttrValueListBySpu(productId);
    }

}
