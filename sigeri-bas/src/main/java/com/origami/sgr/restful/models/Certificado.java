/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Origami
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Certificado implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Long numcertificado;
    private Long fechaemision;
    private Integer tipocertificado;
    private Integer printonline;
    private String codeverificate;
    private Integer secuencia;

    public Certificado() {
    }

    public Long getNumcertificado() {
        return numcertificado;
    }

    public void setNumcertificado(Long numcertificado) {
        this.numcertificado = numcertificado;
    }

    public Long getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Long fechaemision) {
        this.fechaemision = fechaemision;
    }

    public Integer getTipocertificado() {
        return tipocertificado;
    }

    public void setTipocertificado(Integer tipocertificado) {
        this.tipocertificado = tipocertificado;
    }

    public Integer getPrintonline() {
        return printonline;
    }

    public void setPrintonline(Integer printonline) {
        this.printonline = printonline;
    }

    public String getCodeverificate() {
        return codeverificate;
    }

    public void setCodeverificate(String codeverificate) {
        this.codeverificate = codeverificate;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }
    
    
    
    
}
