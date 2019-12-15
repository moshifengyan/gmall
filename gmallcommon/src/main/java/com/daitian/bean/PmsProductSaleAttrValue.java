package com.daitian.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

public class PmsProductSaleAttrValue {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long saleAttrId;

    private String saleAttrValueName;

    @Transient
    Integer isChecked;

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public PmsProductSaleAttrValue(Long id, Long productId, Long saleAttrId, String saleAttrValueName) {
        this.id = id;
        this.productId = productId;
        this.saleAttrId = saleAttrId;
        this.saleAttrValueName = saleAttrValueName;
    }

    public PmsProductSaleAttrValue() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSaleAttrId() {
        return saleAttrId;
    }

    public void setSaleAttrId(Long saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public String getSaleAttrValueName() {
        return saleAttrValueName;
    }

    public void setSaleAttrValueName(String saleAttrValueName) {
        this.saleAttrValueName = saleAttrValueName == null ? null : saleAttrValueName.trim();
    }
}