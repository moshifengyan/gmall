package com.daitian.mapper;

import com.daitian.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsBaseAttrInfo record);

    int insertSelective(PmsBaseAttrInfo record);

    PmsBaseAttrInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsBaseAttrInfo record);

    int updateByPrimaryKey(PmsBaseAttrInfo record);

    List<PmsBaseAttrInfo> selectAttrValueListByValueId(@Param(value = "valueIdStr") String valueIdStr);
}