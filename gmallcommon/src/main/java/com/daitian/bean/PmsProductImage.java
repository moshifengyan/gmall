package com.daitian.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class PmsProductImage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String imgName;

    private String imgUrl;

    public PmsProductImage(Long id, Long productId, String imgName, String imgUrl) {
        this.id = id;
        this.productId = productId;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public PmsProductImage() {
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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName == null ? null : imgName.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }
}