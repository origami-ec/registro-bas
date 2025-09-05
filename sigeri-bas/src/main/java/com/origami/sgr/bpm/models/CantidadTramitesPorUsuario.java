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
public class CantidadTramitesPorUsuario implements Serializable {

    public static final Long serialVersionUID = 1L;
    
    protected Date    fecha;
    protected Integer vencidoElab;
    protected Integer elaborados;
    protected Integer cantidad;
    protected String  funcionario;

    public CantidadTramitesPorUsuario() {
        vencidoElab = 0;
        elaborados  = 0;
        cantidad    = 0;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getVencidoElab() {
        return vencidoElab;
    }

    public void setVencidoElab(Integer vencidoElab) {
        this.vencidoElab = vencidoElab;
    }

    public Integer getElaborados() {
        return elaborados;
    }

    public void setElaborados(Integer elaborados) {
        this.elaborados = elaborados;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
    
}
