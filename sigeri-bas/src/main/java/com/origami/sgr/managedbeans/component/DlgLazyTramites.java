/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.lazymodels.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class DlgLazyTramites implements Serializable {

    protected LazyModel lazy;

    @PostConstruct
    protected void initView() {
        lazy = new LazyModel(HistoricoTramites.class, "numTramite", "DESC");
    }

    public void selectTramite(HistoricoTramites ht) {
        PrimeFaces.current().dialog().closeDynamic(ht);
    }

    public LazyModel getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel lazy) {
        this.lazy = lazy;
    }

}
