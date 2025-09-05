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

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_liquidacion_exoneracion", schema = "flow")
public class RegpLiquidacionExoneracion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion liquidacion;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpExoneracion exoneracion;
    @Column(name = "beneficiario")
    private Boolean beneficiario = false;
    @Column(name = "estado")
    private Boolean estado = true;

    public RegpLiquidacionExoneracion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public RegpExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Boolean getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Boolean beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof RegpLiquidacionExoneracion)) {
            return false;
        }
        RegpLiquidacionExoneracion other = (RegpLiquidacionExoneracion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpLiquidacionExoneracion[ id=" + id + " ]";
    }
    
}
