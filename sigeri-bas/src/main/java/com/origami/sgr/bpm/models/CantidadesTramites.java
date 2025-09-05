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
public class CantidadesTramites implements Serializable {
    
    public static final Long serialVersionUID = 1L;
    
    protected Date fecha;
    protected Integer entregados;
    protected Integer recibidos;
    protected Integer incertificado;
    protected Integer ininscripcion; 
    protected Integer vencidos;
    protected Integer pendientes;
    protected Integer outcertificado;
    protected Integer outinscripcion;

    public CantidadesTramites() {
        entregados = 0;
        recibidos = 0;
        vencidos = 0;
        pendientes = 0;
        incertificado = 0;
        ininscripcion = 0;
        outcertificado = 0;
        outinscripcion = 0;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getEntregados() {
        return entregados;
    }

    public void setEntregados(Integer entregados) {
        this.entregados = entregados;
    }

    public Integer getRecibidos() {
        return recibidos;
    }

    public void setRecibidos(Integer recibidos) {
        this.recibidos = recibidos;
    }

    public Integer getVencidos() {
        return vencidos;
    }

    public void setVencidos(Integer vencidos) {
        this.vencidos = vencidos;
    }

    public Integer getPendientes() {
        return pendientes;
    }

    public void setPendientes(Integer pendientes) {
        this.pendientes = pendientes;
    }

    public Integer getIncertificado() {
        return incertificado;
    }

    public void setIncertificado(Integer incertificado) {
        this.incertificado = incertificado;
    }

    public Integer getIninscripcion() {
        return ininscripcion;
    }

    public void setIninscripcion(Integer ininscripcion) {
        this.ininscripcion = ininscripcion;
    }

    public Integer getOutcertificado() {
        return outcertificado;
    }

    public void setOutcertificado(Integer outcertificado) {
        this.outcertificado = outcertificado;
    }

    public Integer getOutinscripcion() {
        return outinscripcion;
    }

    public void setOutinscripcion(Integer outinscripcion) {
        this.outinscripcion = outinscripcion;
    }
    
}
