/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author asilva
 */
public class TramitesCorregidos implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Long   tramite;
    protected Date   fechaIngreso;
    protected Date   fechaEntrega;
    protected String asignado = "";
    protected Date   fechaElaboracion;
    protected Date   fechaReIngreso;
    protected String observacion;
    protected Date   fechaIngreso2;
    
    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public Date getFechaReIngreso() {
        return fechaReIngreso;
    }

    public void setFechaReIngreso(Date fechaReIngreso) {
        this.fechaReIngreso = fechaReIngreso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaIngreso2() {
        return fechaIngreso2;
    }

    public void setFechaIngreso2(Date fechaIngreso2) {
        this.fechaIngreso2 = fechaIngreso2;
    }

}
