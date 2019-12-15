package com.daitian.mapper;

import com.daitian.bean.PmsBaseCatalog1;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsBaseCatalog1Mapper extends Mapper<PmsBaseCatalog1> {
    int deleteByPrimaryKey(Integer id);

    int insert(PmsBaseCatalog1 record);

    int insertSelective(PmsBaseCatalog1 record);

    PmsBaseCatalog1 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PmsBaseCatalog1 record);

    int updateByPrimaryKey(PmsBaseCatalog1 record);
}