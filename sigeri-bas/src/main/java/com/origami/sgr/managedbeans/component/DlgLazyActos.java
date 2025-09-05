/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegActoLazy;
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
public class DlgLazyActos implements Serializable {

    private static final Logger LOG = Logger.getLogger(DlgLazyActos.class.getName());
    protected RegActoLazy lazy;
    //protected LazyModel<RegActo> lazy;

    @PostConstruct
    public void initView() {
        try {
            //lazy = new LazyModel<>(RegActo.class, "nombre");
            lazy = new RegActoLazy(new RegTipoCobroActo(1L)); //ACTOS DEL SIRE
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "PostConstruct", e);
        }
    }

    public void selectObject(RegActo a) {
        PrimeFaces.current().dialog().closeDynamic(a);
    }

    /*public LazyModel<RegActo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RegActo> lazy) {
        this.lazy = lazy;
    }*/

    public RegActoLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegActoLazy lazy) {
        this.lazy = lazy;
    }

}
