package com.daitian.mapper;

import com.daitian.bean.PmsBaseSaleAttr;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsBaseSaleAttrMapper extends Mapper<PmsBaseSaleAttr> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseSaleAttr record);

    int insertSelective(PmsBaseSaleAttr record);

    PmsBaseSaleAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseSaleAttr record);

    int updateByPrimaryKey(PmsBaseSaleAttr record);
}