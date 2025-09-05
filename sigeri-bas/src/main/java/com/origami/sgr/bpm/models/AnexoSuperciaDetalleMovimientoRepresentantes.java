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
 * @author Arturo
 */
public class AnexoSuperciaDetalleMovimientoRepresentantes implements Serializable {
   private String nombres;
   private String ced_ruc;
   private String domicilio;
   private String cargo;
   private String atribucion;
   private Integer tiempo;
   private String cod_tiempo;
   private Date tiempo_cargo;

    public AnexoSuperciaDetalleMovimientoRepresentantes() {
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

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAtribucion() {
        return atribucion;
    }

    public void setAtribucion(String atribucion) {
        this.atribucion = atribucion;
    }

    

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public String getCod_tiempo() {
        return cod_tiempo;
    }

    public void setCod_tiempo(String cod_tiempo) {
        this.cod_tiempo = cod_tiempo;
    }

    public Date getTiempo_cargo() {
        return tiempo_cargo;
    }

    public void setTiempo_cargo(Date tiempo_cargo) {
        this.tiempo_cargo = tiempo_cargo;
    }

    @Override
    public String toString() {
        return "AnexoSuperciaDetalleMovimientoRepresentantes{" + "nombres=" + nombres + ", ced_ruc=" + ced_ruc + ", domicilio=" + domicilio + ", cargo=" + cargo + ", atribucion=" + atribucion + ", tiempo=" + tiempo + ", cod_tiempo=" + cod_tiempo + ", tiempo_cargo=" + tiempo_cargo + '}';
    }
   
   
   
}
