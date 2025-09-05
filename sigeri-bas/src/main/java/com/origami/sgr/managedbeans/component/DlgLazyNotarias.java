/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.UserSession;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.Serializable;
import java.util.Date;
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
public class DlgLazyNotarias implements Serializable {

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;
    
    protected RegEnteJudiciales juzgado;
    protected LazyModel<RegEnteJudiciales> lazy;

    @PostConstruct
    protected void initView() {
        juzgado = new RegEnteJudiciales();
        lazy = new LazyModel(RegEnteJudiciales.class, "nombre");
    }

    public void saveJuzgado() {
        try {
            if (juzgado.getNombre() != null && !juzgado.getNombre().trim().isEmpty()) {
                juzgado.setAbreviatura(juzgado.getAbreviatura().toUpperCase());
                juzgado.setNombre(juzgado.getNombre().toUpperCase());
                juzgado.setUserCreacion(us.getName_user());
                juzgado.setFechaCreacion(new Date());
                RegEnteJudiciales j = (RegEnteJudiciales) em.persist(juzgado);
                juzgado = new RegEnteJudiciales();
                PrimeFaces.current().dialog().closeDynamic(j);
            } else {
                JsfUti.messageWarning(null, Messages.camposObligatorios, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void selectObject(RegEnteJudiciales e) {
        PrimeFaces.current().dialog().closeDynamic(e);
    }

    public LazyModel<RegEnteJudiciales> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RegEnteJudiciales> lazy) {
        this.lazy = lazy;
    }

    public RegEnteJudiciales getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(RegEnteJudiciales juzgado) {
        this.juzgado = juzgado;
    }

}
