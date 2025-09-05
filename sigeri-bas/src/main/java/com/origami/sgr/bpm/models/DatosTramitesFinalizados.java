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
public class DatosTramitesFinalizados implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Long   tramite;
    protected String factura;
    protected String identificacion;
    protected String cliente;
    protected String userResponsable;
    protected String userCorreccion;
    protected Date   fechaEmision;
    protected Date   fechaIngreso;
    protected Date   fechaEntrega;
    protected Date   fechaElaboracion;
    protected Date   fechaReingreso;
    protected String motivo = "";

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getUserResponsable() {
        return userResponsable;
    }

    public void setUserResponsable(String userResponsable) {
        this.userResponsable = userResponsable;
    }

    public String getUserCorreccion() {
        return userCorreccion;
    }

    public void setUserCorreccion(String userCorreccion) {
        this.userCorreccion = userCorreccion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
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

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
}
