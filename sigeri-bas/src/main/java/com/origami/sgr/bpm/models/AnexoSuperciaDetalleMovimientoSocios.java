/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Arturo
 */
public class AnexoSuperciaDetalleMovimientoSocios implements Serializable{
    public String nombres;
    public String ced_ruc;
    public String tipo_participacion;
    public BigDecimal cantidad;
    public BigDecimal valor;

    public AnexoSuperciaDetalleMovimientoSocios() {
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCed_ruc() {
        return ced_ruc;
    }

    public void setCed_ruc(String ced_ruc) {
        this.ced_ruc = ced_ruc;
    }

    public String getTipo_participacion() {
        return tipo_participacion;
    }

    public void setTipo_participacion(String tipo_participacion) {
        this.tipo_participacion = tipo_participacion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "AnexoSuperciaDetalleMovimientoSocios{" + "nombres=" + nombres + ", ced_ruc=" + ced_ruc + ", tipo_participacion=" + tipo_participacion + ", cantidad=" + cantidad + ", valor=" + valor + '}';
    }
    
    
    
}
