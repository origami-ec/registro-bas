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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_pago_rubro", schema = "financiero")
@NamedQueries({
    @NamedQuery(name = "RenPagoRubro.findAll", query = "SELECT r FROM RenPagoRubro r")})
public class RenPagoRubro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegActo rubro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 19, scale = 2)
    private BigDecimal valor;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenPago pago;

    public RenPagoRubro() {
    }

    public RenPagoRubro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegActo getRubro() {
        return rubro;
    }

    public void setRubro(RegActo rubro) {
        this.rubro = rubro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public RenPago getPago() {
        return pago;
    }

    public void setPago(RenPago pago) {
        this.pago = pago;
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
        if (!(object instanceof RenPagoRubro)) {
            return false;
        }
        RenPagoRubro other = (RenPagoRubro) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenPagoRubro[ id=" + id + " ]";
    }
    
}
