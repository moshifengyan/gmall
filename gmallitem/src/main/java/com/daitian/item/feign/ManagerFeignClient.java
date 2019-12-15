package com.daitian.item.feign;

import com.daitian.bean.PmsProductSaleAttr;
import com.daitian.bean.PmsSkuInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by 代天 on 2019/12/1.
 */
@FeignClient(name = "gmallmanager")
public interface ManagerFeignClient {
    @RequestMapping("getSkuById")
    PmsSkuInfo getSkuById(@RequestParam(name = "skuId") String skuId);

    @RequestMapping("spuSaleAttrListCheckBySku")
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@RequestParam(name = "productId")String productId, @RequestParam(name = "skuId")String skuId);

    @RequestMapping("getSkuSaleAttrValueListBySpu")
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam(value = "productId") String productId);
}
