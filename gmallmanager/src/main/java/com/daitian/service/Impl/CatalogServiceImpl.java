package com.daitian.service.Impl;

import com.daitian.bean.PmsBaseCatalog1;
import com.daitian.bean.PmsBaseCatalog2;
import com.daitian.bean.PmsBaseCatalog3;
import com.daitian.mapper.PmsBaseCatalog1Mapper;
import com.daitian.mapper.PmsBaseCatalog2Mapper;
import com.daitian.mapper.PmsBaseCatalog3Mapper;
import com.daitian.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 代天 on 2019/11/30.
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;
    public List<PmsBaseCatalog1> getCatalog1(){
        return pmsBaseCatalog1Mapper.selectAll();
    }
    public List<PmsBaseCatalog2> getCatalog2(String cataLog1Id){
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(Integer.valueOf(cataLog1Id));
        List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2List;
    }
    public List<PmsBaseCatalog3> getCatalog3(String cataLog2Id){
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(Long.valueOf(cataLog2Id));
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);

        return pmsBaseCatalog3s;
    }
}
