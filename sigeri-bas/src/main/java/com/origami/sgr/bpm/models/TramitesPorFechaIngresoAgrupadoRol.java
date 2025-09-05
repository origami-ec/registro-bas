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
public class TramitesPorFechaIngresoAgrupadoRol implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected String usuario = "";
    protected String assigne = "";
    protected String codigouser = "";
    
    protected Long numtramite;
    protected Long cant_actos;
    
    protected Date fechaingreso;
    protected Date fechaentrega;
    
    protected String funcionario = "";
    protected String rol = "";

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAssigne() {
        return assigne;
    }

    public void setAssigne(String assigne) {
        this.assigne = assigne;
    }

    public String getCodigouser() {
        return codigouser;
    }

    public void setCodigouser(String codigouser) {
        this.codigouser = codigouser;
    }

    public Long getNumtramite() {
        return numtramite;
    }

    public void setNumtramite(Long numtramite) {
        this.numtramite = numtramite;
    }

    public Long getCant_actos() {
        return cant_actos;
    }

    public void setCant_actos(Long cant_actos) {
        this.cant_actos = cant_actos;
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

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
}
