package com.daitian.mapper;

import com.daitian.bean.PmsBaseAttrValue;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsBaseAttrValueMapper extends Mapper<PmsBaseAttrValue> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseAttrValue record);

    int insertSelective(PmsBaseAttrValue record);

    PmsBaseAttrValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseAttrValue record);

    int updateByPrimaryKey(PmsBaseAttrValue record);
}