package com.daitian.mapper;

import com.daitian.bean.PmsSkuInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo>{
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId);
}
