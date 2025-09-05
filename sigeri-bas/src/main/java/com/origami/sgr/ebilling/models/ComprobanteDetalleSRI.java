/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

import com.google.gson.annotations.Expose;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteDetalleSRI {

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private Integer cantidad;
    private String descripcion;
    private String detalleAdicional1;
    private String detalleAdicional2;
    private String detalleAdicional3;
    private BigDecimal precioUnitario;
    private BigDecimal subsidio;
    private BigDecimal precioSinSubsidio;
    private BigDecimal descuento;
    private BigDecimal precioTotal;
    @Expose(deserialize = false, serialize = false)
    private List<ComprobantePagoSRI> pagoDetalle;

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalleAdicional1() {
        return detalleAdicional1;
    }

    public void setDetalleAdicional1(String detalleAdicional1) {
        this.detalleAdicional1 = detalleAdicional1;
    }

    public String getDetalleAdicional2() {
        return detalleAdicional2;
    }

    public void setDetalleAdicional2(String detalleAdicional2) {
        this.detalleAdicional2 = detalleAdicional2;
    }

    public String getDetalleAdicional3() {
        return detalleAdicional3;
    }

    public void setDetalleAdicional3(String detalleAdicional3) {
        this.detalleAdicional3 = detalleAdicional3;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(BigDecimal subsidio) {
        this.subsidio = subsidio;
    }

    public BigDecimal getPrecioSinSubsidio() {
        return precioSinSubsidio;
    }

    public void setPrecioSinSubsidio(BigDecimal precioSinSubsidio) {
        this.precioSinSubsidio = precioSinSubsidio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<ComprobantePagoSRI> getPagoDetalle() {
        if (pagoDetalle == null ) pagoDetalle = new ArrayList<>();
        return pagoDetalle;
    }

    public void setPagoDetalle(List<ComprobantePagoSRI> pagoDetalle) {
        this.pagoDetalle = pagoDetalle;
    }

    
    
}
