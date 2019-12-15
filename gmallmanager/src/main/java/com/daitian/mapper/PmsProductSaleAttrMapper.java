package com.daitian.mapper;

import com.daitian.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductSaleAttr record);

    int insertSelective(PmsProductSaleAttr record);

    PmsProductSaleAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductSaleAttr record);

    int updateByPrimaryKey(PmsProductSaleAttr record);

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param(value = "productId") String productId, @Param(value = "skuId")String skuId);
}