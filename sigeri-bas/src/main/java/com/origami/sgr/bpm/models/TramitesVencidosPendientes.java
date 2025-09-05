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
public class TramitesVencidosPendientes implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Long   tramite;
    protected Date   fechaingreso;
    protected Date   fechaEntrega;
    protected String asignado    = "";
    protected String tareaActual = "";
    protected Date   fechaInicioTarea;

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
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

    public String getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(String tareaActual) {
        this.tareaActual = tareaActual;
    }

    public Date getFechaInicioTarea() {
        return fechaInicioTarea;
    }

    public void setFechaInicioTarea(Date fechaInicioTarea) {
        this.fechaInicioTarea = fechaInicioTarea;
    }
    
}
