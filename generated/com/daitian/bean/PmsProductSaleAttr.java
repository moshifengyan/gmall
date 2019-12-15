package com.daitian.search.bean;

public class PmsProductSaleAttr {
    private Long id;

    private Long productId;

    private Long saleAttrId;

    private String saleAttrName;

    public PmsProductSaleAttr(Long id, Long productId, Long saleAttrId, String saleAttrName) {
        this.id = id;
        this.productId = productId;
        this.saleAttrId = saleAttrId;
        this.saleAttrName = saleAttrName;
    }

    public PmsProductSaleAttr() {
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

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName == null ? null : saleAttrName.trim();
    }
}