/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Origami
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificadoTramite implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Long numerotramite;
    private Long fechaingreso;
    private String nombresolicitante;
    private String cisolicitante;
    private List<Certificado> certificados;

    public CertificadoTramite() {
    }

    public Long getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(Long numerotramite) {
        this.numerotramite = numerotramite;
    }

    public Long getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Long fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public String getNombresolicitante() {
        return nombresolicitante;
    }

    public void setNombresolicitante(String nombresolicitante) {
        this.nombresolicitante = nombresolicitante;
    }

    public String getCisolicitante() {
        return cisolicitante;
    }

    public void setCisolicitante(String cisolicitante) {
        this.cisolicitante = cisolicitante;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }
    
}
