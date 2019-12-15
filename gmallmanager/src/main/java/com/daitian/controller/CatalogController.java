package com.daitian.controller;

import com.daitian.bean.PmsBaseCatalog1;
import com.daitian.bean.PmsBaseCatalog2;
import com.daitian.bean.PmsBaseCatalog3;
import com.daitian.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@CrossOrigin
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3> catalog3s = catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }


    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){

        List<PmsBaseCatalog2> catalog2s = catalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }

    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){

        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }
}
