package com.daitian.mapper;

import com.daitian.bean.PmsProductInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsProductInfoMapper extends Mapper<PmsProductInfo> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductInfo record);

    int insertSelective(PmsProductInfo record);

    PmsProductInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductInfo record);

    int updateByPrimaryKey(PmsProductInfo record);
}