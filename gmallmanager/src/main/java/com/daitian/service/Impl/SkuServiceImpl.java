package com.daitian.service.Impl;

import com.alibaba.fastjson.JSON;
import com.daitian.bean.*;
import com.daitian.mapper.*;
import com.daitian.service.SkuService;
import com.daitian.util.RedisPoolUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import io.searchbox.indices.DeleteIndex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 代天 on 2019/12/1.
 */
@Service
public class SkuServiceImpl implements SkuService{

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

//    @Autowired
//    SkuRespository skuRespository;

    @Autowired
    JestClient jestClient;//java elasticsearch客户端

    @Autowired
    RedisPoolUtil redisPoolUtil;
    @Override
    @Transactional
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuInfo
        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImage.setProductImgId(pmsSkuImage.getSpuImgId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

        // 发出商品的缓存同步消息
        // 发出商品的搜索引擎的同步消息
    }

    @Override
    public PmsSkuInfo getSkuByIdFromDB(String skuId){
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
        PmsSkuInfo returnPmsSkuInfo =  pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
        pmsSkuSaleAttrValue.setSkuId(skuId);
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
        returnPmsSkuInfo.setSkuImageList(pmsSkuImageList);
        returnPmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValueList);
        return returnPmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId){
        //从连接池中获得redis连接
        Jedis jedis = redisPoolUtil.getJedis();
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        try{
            String key = "sku:"+skuId+":info";
            String lockKey = "sku"+skuId+":lock";
            String infoJson = jedis.get(key);
            String lockToken;
            if(StringUtils.isNotBlank(infoJson)){//如果redis中有sku数据，直接将redis中数据解析为PmsSkuInfo
                pmsSkuInfo = JSON.parseObject(infoJson,PmsSkuInfo.class);
            }else{//如果redis中没有数据，再从mysql中查询
                String token = UUID.randomUUID().toString();
                String lock = jedis.set(lockKey,token,"nx","px",10*1000);//获取分布式锁
                if(StringUtils.isNotBlank(lock)&&lock.equals("OK")){//获取锁成功
                    pmsSkuInfo = getSkuByIdFromDB(skuId);
                    if(pmsSkuInfo!=null){//如果从数据库查询到了数据，将数据写入redis
                        jedis.set(key, JSON.toJSONString(pmsSkuInfo));
                    }else{//如果数据库中也没有数据，将null值写入redis并设置过期时间，防止缓存穿透
                        jedis.setex(key,60*3,JSON.toJSONString(""));
                    }
                    if(StringUtils.isNotBlank(lockToken=jedis.get(lockKey))&&lockToken == token){
                        jedis.eval("ReleaseLock");
                    }
                }else{//获取锁失败的话，自旋等待
                    try {
                        Thread.sleep(3*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return getSkuById(skuId);//自旋再次尝试获取锁
                }
            }
            return pmsSkuInfo;
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam(value = "productId") String productId){
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Scheduled(cron="0 0/1 * * * ?")
    @Override
    public void updateElastic(){
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(select);
        }
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<PmsSearchSkuInfo>();
        for (PmsSkuInfo pmsSkuInfo:pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setSkuAttrValueList(pmsSkuInfo.getSkuAttrValueList());
            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }
        DeleteIndex deleteIndex = new DeleteIndex.Builder("mygmall").build();
        try {
            jestClient.execute(deleteIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PmsSearchSkuInfo pmsSearchSkuInfo:pmsSearchSkuInfoList) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("mygmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();
            try {
                jestClient.execute(put);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("更新elasticsearch成功");
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal productPrice) {
        boolean b = false;

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        BigDecimal price = pmsSkuInfo1.getPrice();

        if(price.compareTo(productPrice)==0){
            b = true;
        }


        return b;
    }
}
