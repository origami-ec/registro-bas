/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_pago", schema = "financiero")
@NamedQueries({
    @NamedQuery(name = "RenPago.findAll", query = "SELECT r FROM RenPago r")})
public class RenPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion liquidacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 19, scale = 2)
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado", nullable = false)
    private boolean estado;
    @JoinColumn(name = "cajero", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser cajero;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Column(name = "descuento", precision = 19, scale = 2)
    private BigDecimal descuento;
    @Column(name = "recargo", precision = 19, scale = 2)
    private BigDecimal recargo;
    @Column(name = "interes", precision = 19, scale = 2)
    private BigDecimal interes;
    @Size(max = 2147483647)
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    private Collection<RenPagoRubro> renPagoRubroCollection;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    private Collection<RenPagoDetalle> renPagoDetalleCollection;

    public RenPago() {
    }

    public RenPago(Long id) {
        this.id = id;
    }

    public RenPago(Long id, Date fechaPago, boolean estado) {
        this.id = id;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public AclUser getCajero() {
        return cajero;
    }

    public void setCajero(AclUser cajero) {
        this.cajero = cajero;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Collection<RenPagoRubro> getRenPagoRubroCollection() {
        return renPagoRubroCollection;
    }

    public void setRenPagoRubroCollection(Collection<RenPagoRubro> renPagoRubroCollection) {
        this.renPagoRubroCollection = renPagoRubroCollection;
    }

    public Collection<RenPagoDetalle> getRenPagoDetalleCollection() {
        return renPagoDetalleCollection;
    }

    public void setRenPagoDetalleCollection(Collection<RenPagoDetalle> renPagoDetalleCollection) {
        this.renPagoDetalleCollection = renPagoDetalleCollection;
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
        if (!(object instanceof RenPago)) {
            return false;
        }
        RenPago other = (RenPago) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenPago[ id=" + id + " ]";
    }
    
}
