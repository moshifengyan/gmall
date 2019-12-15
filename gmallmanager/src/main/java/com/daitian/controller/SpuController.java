package com.daitian.controller;

import com.daitian.bean.PmsProductImage;
import com.daitian.bean.PmsProductInfo;
import com.daitian.bean.PmsProductSaleAttr;
import com.daitian.service.SpuService;
import com.daitian.util.PmsUploadUtil;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class SpuController {
    @Autowired
    SpuService spuService;

    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }


    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }



    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException, MyException {
        // 将图片或者音视频上传到分布式的文件存储系统
        // 将图片的存储路径返回给页面
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
//        System.out.println(imgUrl);
        return imgUrl;
    }

    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody  PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);

        return "success";
    }

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);

        return pmsProductInfos;
    }

    @RequestMapping("spuSaleAttrListCheckBySku")
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@RequestParam(name = "productId")String productId, @RequestParam(name = "skuId")String skuId){
        return spuService.spuSaleAttrListCheckBySku(productId,skuId);
    }
}
