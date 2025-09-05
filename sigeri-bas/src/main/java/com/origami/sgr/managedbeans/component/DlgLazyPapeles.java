/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.lazymodels.RegPapelLazy;
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
public class DlgLazyPapeles implements Serializable {

    protected RegPapelLazy lazy;

    @PostConstruct
    protected void initView() {
        lazy = new RegPapelLazy();
    }

    public void selectObject(RegPapel p) {
        PrimeFaces.current().dialog().closeDynamic(p);
    }

    public RegPapelLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegPapelLazy lazy) {
        this.lazy = lazy;
    }

}
