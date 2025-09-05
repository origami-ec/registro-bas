/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.UserSession;
import com.origami.sgr.entities.RegpExoneracion;
import com.origami.sgr.lazymodels.RegpExoneracionLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class DlgLazyExoneracion implements Serializable {

    protected RegpExoneracionLazy lazy;
    protected RegpExoneracion exo;
    protected Boolean estado = true;

    @Inject
    private UserSession us;
    @EJB(beanName = "manager")
    private Entitymanager em;

    @PostConstruct
    protected void initView() {
        exo = new RegpExoneracion();
        lazy = new RegpExoneracionLazy(true);
    }

    public void updateLazy() {
        if (estado) {
            lazy = new RegpExoneracionLazy(true);
        } else {
            lazy = new RegpExoneracionLazy();
        }
    }

    public void selectObject(RegpExoneracion e) {
        PrimeFaces.current().dialog().closeDynamic(e);
    }

    public RegpExoneracionLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegpExoneracionLazy lazy) {
        this.lazy = lazy;
    }

    public void showDlgEditExo(RegpExoneracion e) {
        exo = e;
        exo.setEntero(exo.getValor().multiply(BigDecimal.valueOf(100)));
        JsfUti.update("formExo");
        JsfUti.executeJS("PF('dlgExoneracion').show();");
    }

    public void showDlgNewExo() {
        exo = new RegpExoneracion();
        exo.setEntero(exo.getValor().multiply(BigDecimal.valueOf(100)));
        JsfUti.update("formExo");
        JsfUti.executeJS("PF('dlgExoneracion').show();");
    }

    public void saveExoneracion() {
        try {
            if (exo.getConcepto() != null && exo.getBaseLegal() != null && exo.getEntero() != null) {
                if (exo.getId() == null) {
                    exo.setUserIngreso(us.getUserId());
                    exo.setFechaIngreso(new Date());
                } else {
                    exo.setUserEdicion(us.getUserId());
                    exo.setFechaEdicion(new Date());
                }
                exo.setValor(exo.getEntero().divide(BigDecimal.valueOf(100)));
                em.persist(exo);
                this.updateLazy();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgExoneracion').hide();");
            } else {
                JsfUti.messageWarning(null, "Faltan datos obligatorios.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RegpExoneracion getExo() {
        return exo;
    }

    public void setExo(RegpExoneracion exo) {
        this.exo = exo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

}
