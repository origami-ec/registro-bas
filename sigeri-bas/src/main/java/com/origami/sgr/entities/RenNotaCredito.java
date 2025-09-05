/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_nota_credito", schema = "financiero")
@NamedQueries({
    @NamedQuery(name = "RenNotaCredito.findAll", query = "SELECT r FROM RenNotaCredito r")})
public class RenNotaCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion factura;
    @JoinColumn(name = "caja_emision", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenCajero cajaEmision;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "estado")
    private String estado;
    @Column(name = "mensaje")
    private String mensaje;
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "codigo_documento")
    private Long codigoDocumento;
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Column(name = "valor_pendiente")
    private BigDecimal valorPendiente;
    @Column(name = "num_comprobante_modifica")
    private String numeroComprobanteModifica;
    @Column(name = "numero_autorizacion_modifica")
    private String numeroAutorizacionModifica;
    @Column(name = "clave_acceso_modifica")
    private String claveAccesoModifica;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte beneficiario;
    @Column(name = "fecha_factura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    

    public RenNotaCredito() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegpLiquidacion getFactura() {
        return factura;
    }

    public void setFactura(RegpLiquidacion factura) {
        this.factura = factura;
    }

    public RenCajero getCajaEmision() {
        return cajaEmision;
    }

    public void setCajaEmision(RenCajero cajaEmision) {
        this.cajaEmision = cajaEmision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPendiente() {
        return valorPendiente;
    }

    public void setValorPendiente(BigDecimal valorPendiente) {
        this.valorPendiente = valorPendiente;
    }

    public String getNumeroComprobanteModifica() {
        return numeroComprobanteModifica;
    }

    public void setNumeroComprobanteModifica(String numeroComprobanteModifica) {
        this.numeroComprobanteModifica = numeroComprobanteModifica;
    }

    public String getNumeroAutorizacionModifica() {
        return numeroAutorizacionModifica;
    }

    public void setNumeroAutorizacionModifica(String numeroAutorizacionModifica) {
        this.numeroAutorizacionModifica = numeroAutorizacionModifica;
    }

    public String getClaveAccesoModifica() {
        return claveAccesoModifica;
    }

    public void setClaveAccesoModifica(String claveAccesoModifica) {
        this.claveAccesoModifica = claveAccesoModifica;
    }

    public CatEnte getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(CatEnte beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }
    
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RenNotaCredito)) {
            return false;
        }
        RenNotaCredito other = (RenNotaCredito) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public Long getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(Long codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenNotaCredito[ id=" + id + " ]";
    }

}
