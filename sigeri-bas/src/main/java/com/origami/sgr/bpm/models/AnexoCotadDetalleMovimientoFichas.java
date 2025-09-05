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
public class AnexoCotadDetalleMovimientoFichas implements Serializable {
    private Integer numFicha;
    private String linderos;
    private String codigo;

    public AnexoCotadDetalleMovimientoFichas() {
    }

    public Integer getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Integer numFicha) {
        this.numFicha = numFicha;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "AnexoCotadDetalleMovimientoFichas{" + "numFicha=" + numFicha + ", linderos=" + linderos + ", codigo=" + codigo + '}';
    }
    
    
}
