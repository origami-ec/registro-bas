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
 * @author Anyelo
 */
public class ReporteTramitesRp implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Long numtramite;
    protected String nombre = "";
    protected String assigne = "";
    protected String usuario = "";
    protected String idprocess = "";
    protected String revisor = "";
    protected String inscriptor = "";
    protected String cliente = "";
    protected Date fechainicio;
    protected Date fechafin;
    protected Date fechaentrega;
    
    public Long getNumtramite() {
        return numtramite;
    }

    public void setNumtramite(Long numtramite) {
        this.numtramite = numtramite;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAssigne() {
        return assigne;
    }

    public void setAssigne(String assigne) {
        this.assigne = assigne;
    }

    public String getIdprocess() {
        return idprocess;
    }

    public void setIdprocess(String idprocess) {
        this.idprocess = idprocess;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(String inscriptor) {
        this.inscriptor = inscriptor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

}
