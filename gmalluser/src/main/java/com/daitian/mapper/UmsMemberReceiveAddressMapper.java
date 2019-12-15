package com.daitian.mapper;


import com.daitian.bean.UmsMemberReceiveAddress;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface UmsMemberReceiveAddressMapper extends Mapper<UmsMemberReceiveAddress>{
    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberReceiveAddress record);

    int insertSelective(UmsMemberReceiveAddress record);

    UmsMemberReceiveAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsMemberReceiveAddress record);

    int updateByPrimaryKey(UmsMemberReceiveAddress record);
}