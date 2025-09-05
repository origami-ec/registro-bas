/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.UafNacionalidad;
import com.origami.sgr.lazymodels.LazyModel;
import java.io.Serializable;
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
public class DlgLazyNacionalidad implements Serializable {

//    protected UafNacionalidadLazy lazy;
    protected LazyModel<UafNacionalidad> lazy;

    @PostConstruct
    protected void initView() {
//        lazy = new UafNacionalidadLazy();
        lazy = new LazyModel(UafNacionalidad.class);
    }

    public void selectObject(UafNacionalidad ua) {
        if (ua != null) {
            PrimeFaces.current().dialog().closeDynamic(ua);
        }
    }

    public LazyModel<UafNacionalidad> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<UafNacionalidad> lazy) {
        this.lazy = lazy;
    }

}
