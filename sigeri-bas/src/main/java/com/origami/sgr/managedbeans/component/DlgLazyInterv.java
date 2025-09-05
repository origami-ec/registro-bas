/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.lazymodels.RegEnteIntervinienteLazy;
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
public class DlgLazyInterv implements Serializable {

    protected RegEnteIntervinienteLazy lazy;

    @PostConstruct
    protected void initView() {
        lazy = new RegEnteIntervinienteLazy();
    }

    public void selectObject(Object ob) {
        PrimeFaces.current().dialog().closeDynamic(ob);
    }

    public RegEnteIntervinienteLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegEnteIntervinienteLazy lazy) {
        this.lazy = lazy;
    }

}
