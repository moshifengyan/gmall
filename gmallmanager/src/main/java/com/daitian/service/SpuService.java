package com.daitian.service;

import com.daitian.bean.PmsProductImage;
import com.daitian.bean.PmsProductInfo;
import com.daitian.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * Created by 代天 on 2019/11/30.
 */
public interface SpuService {
    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId);
}
