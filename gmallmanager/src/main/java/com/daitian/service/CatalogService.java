package com.daitian.service;

import com.daitian.bean.PmsBaseCatalog1;
import com.daitian.bean.PmsBaseCatalog2;
import com.daitian.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * Created by 代天 on 2019/11/30.
 */
public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();
    List<PmsBaseCatalog2> getCatalog2(String cataLogId1);
    List<PmsBaseCatalog3> getCatalog3(String cataLogId2);
}
