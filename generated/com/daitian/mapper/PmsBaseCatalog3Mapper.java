package com.daitian.mapper;

import com.daitian.search.bean.PmsBaseCatalog3;

public interface PmsBaseCatalog3Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseCatalog3 record);

    int insertSelective(PmsBaseCatalog3 record);

    PmsBaseCatalog3 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseCatalog3 record);

    int updateByPrimaryKey(PmsBaseCatalog3 record);
}