package com.daitian.mapper;

import com.daitian.search.bean.PmsBaseAttrInfo;

public interface PmsBaseAttrInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseAttrInfo record);

    int insertSelective(PmsBaseAttrInfo record);

    PmsBaseAttrInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseAttrInfo record);

    int updateByPrimaryKey(PmsBaseAttrInfo record);
}