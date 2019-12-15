package com.daitian.mapper;

import com.daitian.search.bean.PmsProductImage;

public interface PmsProductImageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductImage record);

    int insertSelective(PmsProductImage record);

    PmsProductImage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductImage record);

    int updateByPrimaryKey(PmsProductImage record);
}