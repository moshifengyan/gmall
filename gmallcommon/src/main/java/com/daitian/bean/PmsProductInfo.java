package com.daitian.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

public class PmsProductInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String description;

    private Long catalog3Id;

    private Long tmId;

    private List<PmsProductImage> spuImageList;

    private List<PmsProductSaleAttr> spuSaleAttrList;

    public List<PmsProductImage> getSpuImageList() {
        return spuImageList;
    }

    public void setSpuImageList(List<PmsProductImage> spuImageList) {
        this.spuImageList = spuImageList;
    }

    public List<PmsProductSaleAttr> getSpuSaleAttrList() {
        return spuSaleAttrList;
    }

    public void setSpuSaleAttrList(List<PmsProductSaleAttr> spuSaleAttrList) {
        this.spuSaleAttrList = spuSaleAttrList;
    }

    public PmsProductInfo(Long id, String productName, String description, Long catalog3Id, Long tmId) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.catalog3Id = catalog3Id;
        this.tmId = tmId;
    }

    public PmsProductInfo() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }
}