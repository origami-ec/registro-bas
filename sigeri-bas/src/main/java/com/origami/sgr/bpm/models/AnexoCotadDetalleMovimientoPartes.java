/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;

/**
 *
 * @author Arturo
 */
public class AnexoCotadDetalleMovimientoPartes implements Serializable {
    private String papel;
    private String ced_ruc ;
    private String nombre;
    private String domicilio;
    private String estado;
    private String tipo_interv;

    public AnexoCotadDetalleMovimientoPartes() {
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getCed_ruc() {
        return ced_ruc;
    }

    public void setCed_ruc(String ced_ruc) {
        this.ced_ruc = ced_ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo_interv() {
        return tipo_interv;
    }

    public void setTipo_interv(String tipo_interv) {
        this.tipo_interv = tipo_interv;
    }

    @Override
    public String toString() {
        return "AnexoCotadDetalleMovimientoPartes{" + "papel=" + papel + ", ced_ruc=" + ced_ruc + ", nombre=" + nombre + ", domicilio=" + domicilio + ", estado=" + estado + ", tipo_interv=" + tipo_interv + '}';
    }
    
}
