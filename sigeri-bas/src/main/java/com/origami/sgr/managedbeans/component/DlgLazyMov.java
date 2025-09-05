/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyMov implements Serializable {

    @Inject
    private ServletSession ss;
    
    protected RegMovimientosLazy lazy;
    
    @PostConstruct
    protected void initView() {
        try {
            lazy = new RegMovimientosLazy();
            if (ss.getParametros() != null) {
                if (ss.getParametros().containsKey("idFicha")) {
                    lazy.addFilter("regMovimientoFichaCollection.ficha", ((Collection) ss.getParametros().get("idFicha")));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectMov(RegMovimiento mov) {
        PrimeFaces.current().dialog().closeDynamic(mov);
    }

    public RegMovimientosLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegMovimientosLazy lazy) {
        this.lazy = lazy;
    }

}
