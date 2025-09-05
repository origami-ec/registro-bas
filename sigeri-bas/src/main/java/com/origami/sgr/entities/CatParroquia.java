/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "cat_parroquia", schema = "app")
@XmlRootElement
public class CatParroquia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_parroquia")
    private BigInteger codigoParroquia;
    @Size(max = 60)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "sedi_parroquia")
    private String sediParroquia;
    @Column(name = "sedi_ubicacion")
    private String sediUbicacion;
    @Column(name = "nrpm_codigo")
    private String nrpmCodigo;
    
    public CatParroquia() {
    }

    public CatParroquia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCodigoParroquia() {
        return codigoParroquia;
    }

    public void setCodigoParroquia(BigInteger codigoParroquia) {
        this.codigoParroquia = codigoParroquia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getSediParroquia() {
        return sediParroquia;
    }

    public void setSediParroquia(String sediParroquia) {
        this.sediParroquia = sediParroquia;
    }

    public String getSediUbicacion() {
        return sediUbicacion;
    }

    public void setSediUbicacion(String sediUbicacion) {
        this.sediUbicacion = sediUbicacion;
    }

    public String getNrpmCodigo() {
        return nrpmCodigo;
    }

    public void setNrpmCodigo(String nrpmCodigo) {
        this.nrpmCodigo = nrpmCodigo;
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
        if (!(object instanceof CatParroquia)) {
            return false;
        }
        CatParroquia other = (CatParroquia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CatParroquia[ id=" + id + " ]";
    }
    
}
