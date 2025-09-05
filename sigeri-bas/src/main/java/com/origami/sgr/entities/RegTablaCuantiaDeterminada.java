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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_tabla_cuantia_determinada", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findAll", query = "SELECT r FROM RegTablaCuantiaDeterminada r"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findById", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.id = :id"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByValorInicial", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.valorInicial = :valorInicial"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByValorFinal", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.valorFinal = :valorFinal"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByTotalCobrar", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.totalCobrar = :totalCobrar"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByExcesoValor", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.excesoValor = :excesoValor"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByValorBase", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.valorBase = :valorBase"),
    @NamedQuery(name = "RegTablaCuantiaDeterminada.findByCantidadBase", query = "SELECT r FROM RegTablaCuantiaDeterminada r WHERE r.cantidadBase = :cantidadBase")})
public class RegTablaCuantiaDeterminada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_inicial")
    private BigDecimal valorInicial;
    @Column(name = "valor_final")
    private BigDecimal valorFinal;
    @Column(name = "total_cobrar")
    private BigDecimal totalCobrar;
    @Column(name = "exceso_valor")
    private BigDecimal excesoValor;
    @Column(name = "valor_base")
    private BigDecimal valorBase;
    @Column(name = "cantidad_base")
    private BigDecimal cantidadBase;

    public RegTablaCuantiaDeterminada() {
    }

    public RegTablaCuantiaDeterminada(Long id) {
        this.id = id;
    }

    public RegTablaCuantiaDeterminada(Long id, BigDecimal valorInicial) {
        this.id = id;
        this.valorInicial = valorInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public BigDecimal getTotalCobrar() {
        return totalCobrar;
    }

    public void setTotalCobrar(BigDecimal totalCobrar) {
        this.totalCobrar = totalCobrar;
    }

    public BigDecimal getExcesoValor() {
        return excesoValor;
    }

    public void setExcesoValor(BigDecimal excesoValor) {
        this.excesoValor = excesoValor;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public BigDecimal getCantidadBase() {
        return cantidadBase;
    }

    public void setCantidadBase(BigDecimal cantidadBase) {
        this.cantidadBase = cantidadBase;
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
        if (!(object instanceof RegTablaCuantiaDeterminada)) {
            return false;
        }
        RegTablaCuantiaDeterminada other = (RegTablaCuantiaDeterminada) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegTablaCuantiaDeterminada[ id=" + id + " ]";
    }
    
}
