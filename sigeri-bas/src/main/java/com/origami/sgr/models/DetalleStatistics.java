/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author gutya
 */
public class DetalleStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private BigInteger pendientes;
    private BigInteger finalizados;

    public DetalleStatistics() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getPendientes() {
        return pendientes;
    }

    public void setPendientes(BigInteger pendientes) {
        this.pendientes = pendientes;
    }

    public BigInteger getFinalizados() {
        return finalizados;
    }

    public void setFinalizados(BigInteger finalizados) {
        this.finalizados = finalizados;
    }

}
