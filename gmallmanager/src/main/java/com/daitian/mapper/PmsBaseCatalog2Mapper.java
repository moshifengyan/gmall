package com.daitian.mapper;

import com.daitian.bean.PmsBaseCatalog2;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsBaseCatalog2Mapper extends Mapper<PmsBaseCatalog2> {
    int deleteByPrimaryKey(Integer id);

    int insert(PmsBaseCatalog2 record);

    int insertSelective(PmsBaseCatalog2 record);

    PmsBaseCatalog2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PmsBaseCatalog2 record);

    int updateByPrimaryKey(PmsBaseCatalog2 record);
}