package com.daitian.search.bean;

import java.util.Date;

public class PmsProductVertifyRecord {
    private Long id;

    private Long productId;

    private Date createTime;

    private String vertifyMan;

    private Integer status;

    private String detail;

    public PmsProductVertifyRecord(Long id, Long productId, Date createTime, String vertifyMan, Integer status, String detail) {
        this.id = id;
        this.productId = productId;
        this.createTime = createTime;
        this.vertifyMan = vertifyMan;
        this.status = status;
        this.detail = detail;
    }

    public PmsProductVertifyRecord() {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVertifyMan() {
        return vertifyMan;
    }

    public void setVertifyMan(String vertifyMan) {
        this.vertifyMan = vertifyMan == null ? null : vertifyMan.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}