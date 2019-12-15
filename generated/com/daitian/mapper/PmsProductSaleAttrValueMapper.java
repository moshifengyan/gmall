package com.daitian.mapper;

import com.daitian.search.bean.PmsProductSaleAttrValue;

public interface PmsProductSaleAttrValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductSaleAttrValue record);

    int insertSelective(PmsProductSaleAttrValue record);

    PmsProductSaleAttrValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductSaleAttrValue record);

    int updateByPrimaryKey(PmsProductSaleAttrValue record);
}