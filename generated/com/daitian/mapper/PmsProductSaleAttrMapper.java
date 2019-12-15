package com.daitian.mapper;

import com.daitian.search.bean.PmsProductSaleAttr;

public interface PmsProductSaleAttrMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductSaleAttr record);

    int insertSelective(PmsProductSaleAttr record);

    PmsProductSaleAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductSaleAttr record);

    int updateByPrimaryKey(PmsProductSaleAttr record);
}