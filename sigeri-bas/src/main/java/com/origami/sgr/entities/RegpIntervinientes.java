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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_intervinientes",  schema = "flow")
@XmlRootElement
public class RegpIntervinientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpExoneracion exoneracion;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacionDetalles liquidacion;
    @JoinColumn(name = "papel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegPapel papel;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @Column(name = "es_beneficiario")
    private Boolean esBeneficiario = false;
    @Column(name = "nombres")
    private String nombres;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsBeneficiario() {
        return esBeneficiario;
    }

    public void setEsBeneficiario(Boolean esBeneficiario) {
        this.esBeneficiario = esBeneficiario;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public RegpExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public RegpLiquidacionDetalles getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacionDetalles liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RegPapel getPapel() {
        return papel;
    }

    public void setPapel(RegPapel papel) {
        this.papel = papel;
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
        if (!(object instanceof RegpIntervinientes)) {
            return false;
        }
        RegpIntervinientes other = (RegpIntervinientes) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpIntervinientes[ id=" + id + " ]";
    }
    
}
