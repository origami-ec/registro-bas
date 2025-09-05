/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.lazymodels.LazyModel;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyDomicilios implements Serializable {

    private static final Logger LOG = Logger.getLogger(DlgLazyDomicilios.class.getName());
    protected LazyModel<RegDomicilio> lazy;

    @PostConstruct
    public void initView() {
        try {
            lazy = new LazyModel<>(RegDomicilio.class, "nombre");
            lazy.getFilterss().put("estado", true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "PostConstruct", e);
        }
    }

    public void selectObject(RegDomicilio a) {
        PrimeFaces.current().dialog().closeDynamic(a);
    }

    public LazyModel<RegDomicilio> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RegDomicilio> lazy) {
        this.lazy = lazy;
    }

}
