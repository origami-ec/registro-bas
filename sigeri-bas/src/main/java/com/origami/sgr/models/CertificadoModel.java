/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Anyelo
 */
public class CertificadoModel implements Serializable {
    
    protected BigInteger idcertificado;
    protected BigInteger numerocertificado;
    protected BigInteger numeroficha;
    protected BigInteger numerotramite;
    protected Date fechaemision;
    protected BigInteger tipocertificado;

    public CertificadoModel() {
    }

    public BigInteger getIdcertificado() {
        return idcertificado;
    }

    public void setIdcertificado(BigInteger idcertificado) {
        this.idcertificado = idcertificado;
    }

    public BigInteger getNumerocertificado() {
        return numerocertificado;
    }

    public void setNumerocertificado(BigInteger numerocertificado) {
        this.numerocertificado = numerocertificado;
    }

    public BigInteger getNumeroficha() {
        return numeroficha;
    }

    public void setNumeroficha(BigInteger numeroficha) {
        this.numeroficha = numeroficha;
    }

    public BigInteger getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(BigInteger numerotramite) {
        this.numerotramite = numerotramite;
    }

    public Date getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Date fechaemision) {
        this.fechaemision = fechaemision;
    }

    public BigInteger getTipocertificado() {
        return tipocertificado;
    }

    public void setTipocertificado(BigInteger tipocertificado) {
        this.tipocertificado = tipocertificado;
    }
    
}
