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
public class AnexoSuperciaDetalleMovimientoCapitales implements Serializable {
    public String nombre;
    public String valor;

    public AnexoSuperciaDetalleMovimientoCapitales() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    

    @Override
    public String toString() {
        return "AnexoSuperciaDetalleMovimientoCapitales{" + "nombre=" + nombre + ", valor=" + valor + '}';
    }
    
    
}
