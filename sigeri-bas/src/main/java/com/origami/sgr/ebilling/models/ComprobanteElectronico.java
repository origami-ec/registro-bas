/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Origami
 */
@XmlRootElement
public class ComprobanteElectronico implements Serializable {

    //DATOS PARA FACTURA
    private Long idLiquidacion;
    private String tipoLiquidacionSGR; //REGPLIQUIDACION (RL) - RENFACTURA (RF) - REGNOTACREDITO (RN)
    private Long tramite;
    private Boolean isOnline;
    private Boolean reenvioVerificacion = Boolean.FALSE; //CAMPO UTILZADO PARA VERIFICAR SI LA CLAVE DE ACCESO FUE REALMENTE AUTORIZADA
    private String ambiente;
    private String puntoEmision;
    private String rucEntidad;
    private String comprobanteCodigo;
    private String numComprobante;
    private String claveAcceso;
    private BigDecimal descuentoAdicional;
    private Cabecera cabecera;
    private List<DetallePago> detallePagos;
    private Detalles detalles;

    //DATOS PARA NOTA DE CREDITO
    private String numComprobanteModifica;
    private String motivoNotaCredito;
    private String tipoDocumentoModifica;
    private String fechaEmisionDocumentoModifica;

    public ComprobanteElectronico() {
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public String getRucEntidad() {
        return rucEntidad;
    }

    public void setRucEntidad(String rucEntidad) {
        this.rucEntidad = rucEntidad;
    }

    public String getComprobanteCodigo() {
        return comprobanteCodigo;
    }

    public void setComprobanteCodigo(String comprobanteCodigo) {
        this.comprobanteCodigo = comprobanteCodigo;
    }

    public String getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(String numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public Cabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(Cabecera cabecera) {
        this.cabecera = cabecera;
    }

    public List<DetallePago> getDetallePagos() {
        return detallePagos;
    }

    public void setDetallePagos(List<DetallePago> detallePagos) {
        this.detallePagos = detallePagos;
    }

    public Detalles getDetalles() {
        return detalles;
    }

    public void setDetalles(Detalles detalles) {
        this.detalles = detalles;
    }

    public String getNumComprobanteModifica() {
        return numComprobanteModifica;
    }

    public void setNumComprobanteModifica(String numComprobanteModifica) {
        this.numComprobanteModifica = numComprobanteModifica;
    }

    public String getMotivoNotaCredito() {
        return motivoNotaCredito;
    }

    public void setMotivoNotaCredito(String motivoNotaCredito) {
        this.motivoNotaCredito = motivoNotaCredito;
    }

    public String getTipoDocumentoModifica() {
        return tipoDocumentoModifica;
    }

    public void setTipoDocumentoModifica(String tipoDocumentoModifica) {
        this.tipoDocumentoModifica = tipoDocumentoModifica;
    }

    public String getFechaEmisionDocumentoModifica() {
        return fechaEmisionDocumentoModifica;
    }

    public void setFechaEmisionDocumentoModifica(String fechaEmisionDocumentoModifica) {
        this.fechaEmisionDocumentoModifica = fechaEmisionDocumentoModifica;
    }

    public BigDecimal getDescuentoAdicional() {
        if (descuentoAdicional == null) {
            descuentoAdicional = BigDecimal.ZERO;
        }
        return descuentoAdicional;
    }

    public void setDescuentoAdicional(BigDecimal descuentoAdicional) {
        this.descuentoAdicional = descuentoAdicional;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Long getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Long idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getTipoLiquidacionSGR() {
        return tipoLiquidacionSGR;
    }

    public void setTipoLiquidacionSGR(String tipoLiquidacionSGR) {
        this.tipoLiquidacionSGR = tipoLiquidacionSGR;
    }

    public Boolean getReenvioVerificacion() {
        return reenvioVerificacion;
    }

    public void setReenvioVerificacion(Boolean reenvioVerificacion) {
        this.reenvioVerificacion = reenvioVerificacion;
    }

    @Override
    public String toString() {
        return "ComprobanteElectronico{" + "idLiquidacion=" + idLiquidacion + ", tipoLiquidacionSGR=" + tipoLiquidacionSGR + ", tramite=" + tramite + ", isOnline=" + isOnline + ", reenvioVerificacion=" + reenvioVerificacion + ", ambiente=" + ambiente + ", puntoEmision=" + puntoEmision + ", rucEntidad=" + rucEntidad + ", comprobanteCodigo=" + comprobanteCodigo + ", numComprobante=" + numComprobante + ", claveAcceso=" + claveAcceso + ", descuentoAdicional=" + descuentoAdicional + ", cabecera=" + cabecera + ", detallePagos=" + detallePagos + ", detalles=" + detalles + ", numComprobanteModifica=" + numComprobanteModifica + ", motivoNotaCredito=" + motivoNotaCredito + ", tipoDocumentoModifica=" + tipoDocumentoModifica + ", fechaEmisionDocumentoModifica=" + fechaEmisionDocumentoModifica + '}';
    }

}
