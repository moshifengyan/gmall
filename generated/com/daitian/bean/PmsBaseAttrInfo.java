package com.daitian.search.bean;

public class PmsBaseAttrInfo {
    private Long id;

    private String attrName;

    private Long catalog3Id;

    private String isEnabled;

    public PmsBaseAttrInfo(Long id, String attrName, Long catalog3Id, String isEnabled) {
        this.id = id;
        this.attrName = attrName;
        this.catalog3Id = catalog3Id;
        this.isEnabled = isEnabled;
    }

    public PmsBaseAttrInfo() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName == null ? null : attrName.trim();
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled == null ? null : isEnabled.trim();
    }
}