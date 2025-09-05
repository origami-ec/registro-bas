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
public class TramitesParaRegistrador implements Serializable{

    public static final Long serialVersionUID = 1L;
    
    protected Long tramite;

    protected Date fechaingreso;
    protected Date fechaentrega;    
    protected Date fechainicio;
    protected Date fechafin;
    
    protected String nombreActo  = "";
    protected String observacion = "";
    
    protected Long   repertorio;
    protected Long   inscripcion;
    
    protected String asignado = "";
    protected String estado   = "";
    
    protected Integer led;

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

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getNombreActo() {
        return nombreActo;
    }

    public void setNombreActo(String nombreActo) {
        this.nombreActo = nombreActo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Long repertorio) {
        this.repertorio = repertorio;
    }

    public Long getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Long inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getAsignado() {
        return asignado;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getLed() {
        return led;
    }

    public void setLed(Integer led) {
        this.led = led;
    }
 
}
