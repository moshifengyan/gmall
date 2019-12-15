package com.daitian.controller;

import com.daitian.bean.PmsBaseAttrInfo;
import com.daitian.bean.PmsBaseAttrValue;
import com.daitian.bean.PmsBaseSaleAttr;
import com.daitian.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class AttrController {

    @Autowired
    AttrService attrService;

    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }

    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        attrService.saveAttrInfoAndValue(pmsBaseAttrInfo);
        return "success";
    }

    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }

    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

    @PostMapping("getAttrValueListByValueIds")
    public List<PmsBaseAttrInfo> getAttrValueListByValueIds(@RequestBody Set<String> valueIdSet){
        return attrService.getAttrValueListByValueIds(valueIdSet);
    }
}
