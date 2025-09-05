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
public class SolicitanteInterviniente implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected String solicitanteInterviniente;
    protected Long   tramite;
    protected String factura;
    protected Date   fechaingreso;
    protected String tramitador   = "";
    protected String estudioJuridico = "";
    protected String infAdicional = "";
    protected double valor_fact  = 0;
    
    public String getSolicitanteInterviniente() {
        return solicitanteInterviniente;
    }

    public void setSolicitanteInterviniente(String solicitanteInterviniente) {
        this.solicitanteInterviniente = solicitanteInterviniente;
    }

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

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public String getTramitador() {
        return tramitador;
    }

    public void setTramitador(String tramitador) {
        this.tramitador = tramitador;
    }

    public String getEstudioJuridico() {
        return estudioJuridico;
    }

    public void setEstudioJuridico(String estudioJuridico) {
        this.estudioJuridico = estudioJuridico;
    }

    public String getInfAdicional() {
        return infAdicional;
    }

    public void setInfAdicional(String infAdicional) {
        this.infAdicional = infAdicional;
    }

    public double getValor_fact() {
        return valor_fact;
    }

    public void setValor_fact(double valor_fact) {
        this.valor_fact = valor_fact;
    }

}
