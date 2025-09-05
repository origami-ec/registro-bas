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
 * @author Origami
 */
public class AppTramite implements Serializable {

    private BigInteger numerotramite;
    private BigInteger fechaingreso;
    private BigInteger fechaentrega;
    private Boolean tipocertificado;
    private Double resta;
    private Double hoy;
    private Double avance;

    public BigInteger getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(BigInteger numerotramite) {
        this.numerotramite = numerotramite;
    }

    public BigInteger getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(BigInteger fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public BigInteger getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(BigInteger fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public Boolean getTipocertificado() {
        return tipocertificado;
    }

    public void setTipocertificado(Boolean tipocertificado) {
        this.tipocertificado = tipocertificado;
    }

    public Double getResta() {
        return resta;
    }

    public void setResta(Double resta) {
        this.resta = resta;
    }

    public Double getHoy() {
        return hoy;
    }

    public void setHoy(Double hoy) {
        this.hoy = hoy;
    }

    public Double getAvance() {
        return avance;
    }

    public void setAvance(Double avance) {
        this.avance = avance;
    }

}
