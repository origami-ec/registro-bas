/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.Feriados;
import com.origami.sgr.lazymodels.FeriadosLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class FeriadosManaged implements Serializable {

    private static final Logger LOG = Logger.getLogger(FeriadosManaged.class.getName());

    @Inject
    protected UserSession us;
    @EJB
    protected Entitymanager em;

    protected FeriadosLazy lazy;
    protected Feriados feriado;

    @PostConstruct
    protected void iniView() {
        try {
            feriado = new Feriados();
            lazy = new FeriadosLazy();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgInferiado() {
        feriado = new Feriados();
        JsfUti.update("formIngreso");
        JsfUti.executeJS("PF('dlgIngresoTramite').show();");
    }

    public void showDlgEdferiado(Feriados fe) {
        feriado = fe;
        JsfUti.update("formIngreso");
        JsfUti.executeJS("PF('dlgIngresoTramite').show();");
    }

    public void saveFeriado() {
        try {
            if (feriado.getFecha() != null && feriado.getDescripcion() != null) {
                feriado.setUserIngreso(us.getUserId());
                feriado.setFechaIngreso(new Date());
                feriado = (Feriados) em.persist(feriado);
                lazy = new FeriadosLazy();
                JsfUti.update("mainForm:dtferiados");
                JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                JsfUti.messageInfo(null, "Ingreso guardado con exito!!!", "");
            } else {
                JsfUti.update("mainForm:dtferiados");
                JsfUti.messageWarning(null, "Falta de ingresar campos obligatorios.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public FeriadosLazy getLazy() {
        return lazy;
    }

    public void setLazy(FeriadosLazy lazy) {
        this.lazy = lazy;
    }

    public Feriados getFeriado() {
        return feriado;
    }

    public void setFeriado(Feriados feriado) {
        this.feriado = feriado;
    }

}
