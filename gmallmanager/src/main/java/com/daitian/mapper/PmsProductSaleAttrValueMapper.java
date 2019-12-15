package com.daitian.mapper;

import com.daitian.bean.PmsProductSaleAttrValue;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsProductSaleAttrValueMapper extends Mapper<PmsProductSaleAttrValue> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductSaleAttrValue record);

    int insertSelective(PmsProductSaleAttrValue record);

    PmsProductSaleAttrValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductSaleAttrValue record);

    int updateByPrimaryKey(PmsProductSaleAttrValue record);
}