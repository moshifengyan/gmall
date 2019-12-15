package com.daitian.service.Impl;

import com.daitian.bean.PmsBaseAttrInfo;
import com.daitian.bean.PmsBaseAttrValue;
import com.daitian.bean.PmsBaseSaleAttr;
import com.daitian.mapper.PmsBaseAttrInfoMapper;
import com.daitian.mapper.PmsBaseAttrValueMapper;
import com.daitian.mapper.PmsBaseSaleAttrMapper;
import com.daitian.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * Created by 代天 on 2019/11/30.
 */
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String cataLog3Id){
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(Long.valueOf(cataLog3Id));
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        //取出平台属性的取值供添加sku是选择
        for (PmsBaseAttrInfo p:pmsBaseAttrInfoList) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(Long.valueOf(p.getId()));
            p.setAttrValueList(pmsBaseAttrValueMapper.select(pmsBaseAttrValue));
        }
        return pmsBaseAttrInfoList;
    }



    @Override
    public void saveAttrInfoAndValue(PmsBaseAttrInfo pmsBaseAttrInfo){
        String id = pmsBaseAttrInfo.getId();
        if(id == null){//没有传id，新建属性信息
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);//insert insertSelective 是否将null插入数据库

            // 保存属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(Long.valueOf(pmsBaseAttrInfo.getId()));
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }else{//传了id，保存信息
            // id不空，修改

            // 属性修改
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);


            // 属性值修改
            // 按照属性id删除所有属性值
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(Long.valueOf(pmsBaseAttrInfo.getId()));
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

            // 删除后，将新的属性值插入
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(Long.valueOf(pmsBaseAttrInfo.getId()));
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(Long.valueOf(attrId));
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueIds(Set<String> valueIdSet) {

        String valueIdStr = StringUtils.join(valueIdSet, ",");//41,45,46
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }
}
