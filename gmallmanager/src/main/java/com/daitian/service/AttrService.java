package com.daitian.service;

import com.daitian.bean.PmsBaseAttrInfo;
import com.daitian.bean.PmsBaseAttrValue;
import com.daitian.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 * Created by 代天 on 2019/11/30.
 */
public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String cataLog3Id);

    void saveAttrInfoAndValue(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueIds(Set<String> valueIdSet);
}
