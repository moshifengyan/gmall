package com.daitian.mapper;

import com.daitian.search.bean.PmsBaseSaleAttr;

public interface PmsBaseSaleAttrMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseSaleAttr record);

    int insertSelective(PmsBaseSaleAttr record);

    PmsBaseSaleAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseSaleAttr record);

    int updateByPrimaryKey(PmsBaseSaleAttr record);
}