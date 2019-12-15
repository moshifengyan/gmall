package com.daitian.service;

import com.daitian.bean.PmsSkuInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 代天 on 2019/12/1.
 */
public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuByIdFromDB(String skuId);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam(value = "productId") String productId);

    void updateElastic();

    boolean checkPrice(String productSkuId, BigDecimal productPrice);
}
