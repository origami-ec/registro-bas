/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_certificado_propietario", schema = "app")
public class RegCertificadoPropietario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "certificado", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegCertificado certificado;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente propietario;
    @Column(name = "documento")
    private String documento;
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 255)
    @Column(name = "calidad")
    private String calidad;

    public RegCertificadoPropietario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegCertificado getCertificado() {
        return certificado;
    }

    public void setCertificado(RegCertificado certificado) {
        this.certificado = certificado;
    }

    public RegEnteInterviniente getPropietario() {
        return propietario;
    }

    public void setPropietario(RegEnteInterviniente propietario) {
        this.propietario = propietario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegCertificadoPropietario)) {
            return false;
        }
        RegCertificadoPropietario other = (RegCertificadoPropietario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegCertificadoPropietario[ id=" + id + " ]";
    }
    
}
