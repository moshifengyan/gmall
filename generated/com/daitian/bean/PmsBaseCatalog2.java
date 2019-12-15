package com.daitian.search.bean;

public class PmsBaseCatalog2 {
    private Integer id;

    private String name;

    private Integer catalog1Id;

    public PmsBaseCatalog2(Integer id, String name, Integer catalog1Id) {
        this.id = id;
        this.name = name;
        this.catalog1Id = catalog1Id;
    }

    public PmsBaseCatalog2() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCatalog1Id() {
        return catalog1Id;
    }

    public void setCatalog1Id(Integer catalog1Id) {
        this.catalog1Id = catalog1Id;
    }
}