/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import com.origami.session.ServletSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegDomicilio;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.UafNacionalidad;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgEditEnteInterviniente extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(DlgEditEnteInterviniente.class.getName());
    private RegMovimientoCliente interv;
    private Collection<RegPapel> regCatPapelByActo;
    @Inject
    private ServletSession servletSession;
    @Inject
    protected RegistroPropiedadServices reg;

    @PostConstruct
    public void initView() {
        try {
            if (servletSession.tieneParametro("interv")) {
                interv = (RegMovimientoCliente) servletSession.getParametros().get("interv");
                if (this.interv.getDomicilio() == null) {
                    this.interv.setDomicilio(new RegDomicilio());
                }
                if (this.interv.getEnteInterv() == null) {
                    this.interv.setEnteInterv(new RegEnteInterviniente());
                }
                if (this.interv.getEnteInterv().getNacionalidad() == null) {
                    this.interv.getEnteInterv().setNacionalidad(new UafNacionalidad());
                }
            } else {

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "PostConstruct", e);
        }
    }

    public void selectObject(SelectEvent event) {
        try {
            if (event.getObject() instanceof UafNacionalidad) {
                interv.getEnteInterv().setNacionalidad((UafNacionalidad) event.getObject());
                interv.setChangeEnte(Boolean.TRUE);
            } else if (event.getObject() instanceof RegDomicilio) {
                interv.setDomicilio((RegDomicilio) event.getObject());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "SelectObject", e);
        }
    }

    public void agregarListaIntervEdit() {
        try {
            if (interv.getPapel() == null) {
                JsfUti.messageInfo(null, "Debe seleccionar el papel.", "");
                return;
            }
            if (interv.getItem() != null) {
                interv.setEstado(interv.getItem().getValor());
            }
            if (this.interv.getDomicilio().getId() == null) {
                this.interv.setDomicilio(null);
            }
            if (this.interv.getEnteInterv().getNacionalidad().getId() == null) {
                this.interv.getEnteInterv().setNacionalidad(null);
            }
            JsfUti.messageInfo(null, Messages.transacExitosa, "");
            this.selectObject();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void selectObject() {
        PrimeFaces.current().dialog().closeDynamic(interv);
    }

    public RegMovimientoCliente getInterv() {
        return interv;
    }

    public void setInterv(RegMovimientoCliente interv) {
        this.interv = interv;
    }

    public Collection<RegPapel> getPapeles() {
        if (servletSession.tieneParametro("acto")) {
            RegActo acto = (RegActo) servletSession.getParametros().get("acto");
            if (acto != null) {
                if (regCatPapelByActo == null) {
                    regCatPapelByActo = reg.getRegCatPapelByActo(acto.getId());
                }
                return regCatPapelByActo;
            }
        }
        return null;
    }

}
