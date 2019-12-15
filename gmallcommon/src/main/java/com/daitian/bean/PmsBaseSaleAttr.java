package com.daitian.bean;

public class PmsBaseSaleAttr {
    private Long id;

    private String name;

    public PmsBaseSaleAttr(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PmsBaseSaleAttr() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}