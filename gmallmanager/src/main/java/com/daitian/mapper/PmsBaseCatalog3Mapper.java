package com.daitian.mapper;

import com.daitian.bean.PmsBaseCatalog3;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsBaseCatalog3Mapper extends Mapper<PmsBaseCatalog3> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseCatalog3 record);

    int insertSelective(PmsBaseCatalog3 record);

    PmsBaseCatalog3 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseCatalog3 record);

    int updateByPrimaryKey(PmsBaseCatalog3 record);
}