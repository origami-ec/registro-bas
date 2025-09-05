/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.lazymodels.RegFichaLazy;
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
public class DlgLazyFichas implements Serializable {

    protected RegFichaLazy lazy;

    @PostConstruct
    protected void initView() {
        lazy = new RegFichaLazy();
    }

    public void selectFicha(RegFicha ficha) {
        PrimeFaces.current().dialog().closeDynamic(ficha);
    }

    public RegFichaLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegFichaLazy lazy) {
        this.lazy = lazy;
    }

}
