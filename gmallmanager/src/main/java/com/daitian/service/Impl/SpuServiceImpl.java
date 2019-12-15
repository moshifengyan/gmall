package com.daitian.service.Impl;

import com.daitian.bean.PmsProductImage;
import com.daitian.bean.PmsProductInfo;
import com.daitian.bean.PmsProductSaleAttr;
import com.daitian.bean.PmsProductSaleAttrValue;
import com.daitian.mapper.PmsProductImageMapper;
import com.daitian.mapper.PmsProductInfoMapper;
import com.daitian.mapper.PmsProductSaleAttrMapper;
import com.daitian.mapper.PmsProductSaleAttrValueMapper;
import com.daitian.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static javafx.beans.binding.Bindings.select;

/**
 * Created by 代天 on 2019/11/30.
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(Long.valueOf(spuId));
        return pmsProductImageMapper.select(pmsProductImage);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(Long.valueOf(spuId));
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr p:pmsProductSaleAttrList) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setSaleAttrId(Long.valueOf(p.getSaleAttrId()));
            pmsProductSaleAttrValue.setProductId(Long.valueOf(spuId));
            p.setSpuSaleAttrValueList(pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue));
        }
        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(Long.valueOf(catalog3Id));
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        // 保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        // 生成商品主键
        Long productId = pmsProductInfo.getId();

        // 保存商品图片信息
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        // 保存销售属性信息
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            // 保存销售属性值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrs;
    }
}
