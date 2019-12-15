package com.daitian.bean;

public class PmsBaseCatalog1 {
    private Integer id;

    private String name;

    public PmsBaseCatalog1(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public PmsBaseCatalog1() {
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
}