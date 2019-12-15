package com.daitian.controller;

import com.daitian.bean.*;
import com.daitian.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.daitian.feign.ManagerFeignClient1;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.util.StringUtil;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@CrossOrigin
public class SearchController {

    @Autowired
    SearchService searchService;

    @Autowired
    ManagerFeignClient1 managerFeignClient;

    @RequestMapping("list")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList=  searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            if(skuAttrValueList!=null){
                for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                    String valueId = pmsSkuAttrValue.getValueId();
                    valueIdSet.add(valueId);
                }
            }

        }
        // 根据valueId将属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = managerFeignClient.getAttrValueListByValueIds(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        // 对平台属性集合进一步处理，去掉当前条件中valueId所在的属性组
        String[] delValueIds = pmsSearchParam.getValueId();
        if (delValueIds != null) {
            // 面包屑
            // pmsSearchParam
            // delValueIds
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                // 生成面包屑的参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId().toString();
                        if (delValueId.equals(valueId)) {
                            // 查找面包屑的属性值名称
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }



        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }
        return "list";
    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValueId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                if (!pmsSkuAttrValue.equals(delValueId)) {
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }
            }
        }

        return urlParam;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {

            for (String pmsSkuAttrValue : skuAttrValueList) {
                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
            }
        }

        return urlParam;
    }

    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap) {
        String memberId = (String)request.getAttribute("memberId");
        if(StringUtils.isNotBlank(memberId)&&memberId!="null"){
            modelMap.put("memberId",memberId);
        }
        return "index";
    }

}
