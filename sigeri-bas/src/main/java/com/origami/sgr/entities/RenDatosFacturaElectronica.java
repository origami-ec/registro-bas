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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ren_datos_factura_electronica", schema = "financiero")
public class RenDatosFacturaElectronica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "tipo_comprobante")
    private String tipoComprobante;
    @Column(name = "ruc")
    private String ruc;
    @Column(name = "ambiente")
    private String ambiente;
    @Column(name = "serie")
    private String serie;
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @Column(name = "valor_min")
    private Integer valorMin;
    @Column(name = "valor_max")
    private Integer valorMax;
    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "direccion_matriz")
    private String direccionMatriz;
    @Column(name = "tipo_impuesto")
    private String tipoImpuesto;
    @Column(name = "tipo_porcentaje_impuesto")
    private String tipoPorcentajeImpuesto;
    @Column(name = "moneda")
    private String moneda;
    @Column(name = "forma_pago")
    private String formaPago;
    @Column(name = "dias_plazo")
    private String diasPlazo;
    @Column(name = "unidad_tiempo")
    private String unidadTiempo;
    @Column(name = "cobro_iva")
    private String cobroIva;
    
    public RenDatosFacturaElectronica() {
    }

    public RenDatosFacturaElectronica(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public Integer getValorMin() {
        return valorMin;
    }

    public void setValorMin(Integer valorMin) {
        this.valorMin = valorMin;
    }

    public Integer getValorMax() {
        return valorMax;
    }

    public void setValorMax(Integer valorMax) {
        this.valorMax = valorMax;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public String getTipoPorcentajeImpuesto() {
        return tipoPorcentajeImpuesto;
    }

    public void setTipoPorcentajeImpuesto(String tipoPorcentajeImpuesto) {
        this.tipoPorcentajeImpuesto = tipoPorcentajeImpuesto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getDiasPlazo() {
        return diasPlazo;
    }

    public void setDiasPlazo(String diasPlazo) {
        this.diasPlazo = diasPlazo;
    }

    public String getUnidadTiempo() {
        return unidadTiempo;
    }

    public void setUnidadTiempo(String unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }

    public String getCobroIva() {
        return cobroIva;
    }

    public void setCobroIva(String cobroIva) {
        this.cobroIva = cobroIva;
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
        if (!(object instanceof RenDatosFacturaElectronica)) {
            return false;
        }
        RenDatosFacturaElectronica other = (RenDatosFacturaElectronica) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenDatosFacturaElectronica[ id=" + id + " ]";
    }
}
