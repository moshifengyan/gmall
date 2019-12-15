package com.daitian.mapper;

import com.daitian.bean.PmsProductImage;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface PmsProductImageMapper extends Mapper<PmsProductImage> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductImage record);

    int insertSelective(PmsProductImage record);

    PmsProductImage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductImage record);

    int updateByPrimaryKey(PmsProductImage record);
}