package com.daitian.service;

import com.daitian.bean.PmsSearchParam;
import com.daitian.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
