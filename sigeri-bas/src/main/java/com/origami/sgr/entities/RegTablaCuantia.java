/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_tabla_cuantia", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegTablaCuantia.findAll", query = "SELECT r FROM RegTablaCuantia r"),
    @NamedQuery(name = "RegTablaCuantia.findById", query = "SELECT r FROM RegTablaCuantia r WHERE r.id = :id"),
    @NamedQuery(name = "RegTablaCuantia.findByValor1", query = "SELECT r FROM RegTablaCuantia r WHERE r.valor1 = :valor1"),
    @NamedQuery(name = "RegTablaCuantia.findByValor2", query = "SELECT r FROM RegTablaCuantia r WHERE r.valor2 = :valor2"),
    @NamedQuery(name = "RegTablaCuantia.findByCancelar", query = "SELECT r FROM RegTablaCuantia r WHERE r.cancelar = :cancelar"),
    @NamedQuery(name = "RegTablaCuantia.findByCantidadBase", query = "SELECT r FROM RegTablaCuantia r WHERE r.cantidadBase = :cantidadBase"),
    @NamedQuery(name = "RegTablaCuantia.findByValorBase", query = "SELECT r FROM RegTablaCuantia r WHERE r.valorBase = :valorBase"),
    @NamedQuery(name = "RegTablaCuantia.findByExceso", query = "SELECT r FROM RegTablaCuantia r WHERE r.exceso = :exceso")})
public class RegTablaCuantia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor1")
    private BigDecimal valor1;
    @Column(name = "valor2")
    private BigDecimal valor2;
    @Column(name = "cancelar")
    private BigDecimal cancelar;
    @Column(name = "cantidad_base")
    private BigDecimal cantidadBase;
    @Column(name = "valor_base")
    private BigDecimal valorBase;
    @Column(name = "exceso")
    private BigDecimal exceso;

    public RegTablaCuantia() {
    }

    public RegTablaCuantia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor1() {
        return valor1;
    }

    public void setValor1(BigDecimal valor1) {
        this.valor1 = valor1;
    }

    public BigDecimal getValor2() {
        return valor2;
    }

    public void setValor2(BigDecimal valor2) {
        this.valor2 = valor2;
    }

    public BigDecimal getCancelar() {
        return cancelar;
    }

    public void setCancelar(BigDecimal cancelar) {
        this.cancelar = cancelar;
    }

    public BigDecimal getCantidadBase() {
        return cantidadBase;
    }

    public void setCantidadBase(BigDecimal cantidadBase) {
        this.cantidadBase = cantidadBase;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public BigDecimal getExceso() {
        return exceso;
    }

    public void setExceso(BigDecimal exceso) {
        this.exceso = exceso;
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
        if (!(object instanceof RegTablaCuantia)) {
            return false;
        }
        RegTablaCuantia other = (RegTablaCuantia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegTablaCuantia[ id=" + id + " ]";
    }
    
}
