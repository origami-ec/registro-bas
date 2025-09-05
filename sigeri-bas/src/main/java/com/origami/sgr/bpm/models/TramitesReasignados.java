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
public class TramitesReasignados implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Long   tramite;
    protected Date   fechaIngreso;
    protected Date   fechaEntrega;
    protected String asignado   = "";
    protected String reAsignado = "";
    protected String userCreador= "";
    protected Date   fechaReAsignacion;
    protected Date   fechaFinTarea;
    protected String tarea;

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

    public String getReAsignado() {
        return reAsignado;
    }

    public void setReAsignado(String reAsignado) {
        this.reAsignado = reAsignado;
    }

    public String getUserCreador() {
        return userCreador;
    }

    public void setUserCreador(String userCreador) {
        this.userCreador = userCreador;
    }

    public Date getFechaReAsignacion() {
        return fechaReAsignacion;
    }

    public void setFechaReAsignacion(Date fechaReAsignacion) {
        this.fechaReAsignacion = fechaReAsignacion;
    }

    public Date getFechaFinTarea() {
        return fechaFinTarea;
    }

    public void setFechaFinTarea(Date fechaFinTarea) {
        this.fechaFinTarea = fechaFinTarea;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }
    
}
