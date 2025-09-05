/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_factura", schema = "financiero")
public class RenFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion liquidacion;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenPago pago;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "total_pagar")
    private BigDecimal totalPagar;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte solicitante;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name = "user_anulacion")
    private Long userAnulacion;
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @Column(name = "codigo_comprobante")
    private String codigoComprobante;
    @Column(name = "numero_comprobante")
    private BigInteger numeroComprobante;
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "caja", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenCajero caja;
    @Column(name = "forma_pago")
    private String formaPago;
    @Column(name = "estado_ws")
    private String estadoWs;
    @Column(name = "mensaje_ws")
    private String mensajeWs;
    @Column(name = "estado")
    private Boolean estado = true;
    @Transient
    private String ruc;
    @Transient
    private List<RegpLiquidacionDetalles> liquidacionDetalles;
    @Column(name = "factura_sin_tramite")
    private Boolean facturaSinTramite = Boolean.FALSE;

    public RenFactura() {
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

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public Long getUserAnulacion() {
        return userAnulacion;
    }

    public void setUserAnulacion(Long userAnulacion) {
        this.userAnulacion = userAnulacion;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public RenCajero getCaja() {
        return caja;
    }

    public void setCaja(RenCajero caja) {
        this.caja = caja;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getEstadoWs() {
        return estadoWs;
    }

    public void setEstadoWs(String estadoWs) {
        this.estadoWs = estadoWs;
    }

    public String getMensajeWs() {
        return mensajeWs;
    }

    public void setMensajeWs(String mensajeWs) {
        this.mensajeWs = mensajeWs;
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
        if (!(object instanceof RenFactura)) {
            return false;
        }
        RenFactura other = (RenFactura) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenFactura[ id=" + id + " ]";
    }

    public RenPago getPago() {
        return pago;
    }

    public void setPago(RenPago pago) {
        this.pago = pago;
    }

    public List<RegpLiquidacionDetalles> getLiquidacionDetalles() {
        return liquidacionDetalles;
    }

    public void setLiquidacionDetalles(List<RegpLiquidacionDetalles> liquidacionDetalles) {
        this.liquidacionDetalles = liquidacionDetalles;
    }

    public Boolean getFacturaSinTramite() {
        return facturaSinTramite;
    }

    public void setFacturaSinTramite(Boolean facturaSinTramite) {
        this.facturaSinTramite = facturaSinTramite;
    }

}
