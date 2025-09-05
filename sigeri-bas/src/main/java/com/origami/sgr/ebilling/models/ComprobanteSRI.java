/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Origami
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComprobanteSRI implements Serializable {

    private String _id;
    private ComprobanteEntidadSRI entidad;
    private ComprobanteEntidadSRI contribuyente;
    private String tipoComprobante;
    private String codigoTipoComprobante;
    private String numFactura;
    private String numFacturaFormato;
    private BigInteger numComprobante;
    private String numAutorizacion;
    private String fechaAutorizacion;
    private String ambiente;
    private String emision;
    private String claveAcceso;
    private String fechaEmision;
    private Long tramite;

    private BigDecimal subTotal12;
    private BigDecimal subTotalIva;
    private BigDecimal subTotalNoObjetoIva;
    private BigDecimal subTotalExcentoIva;
    private BigDecimal subTotalSinImpuetos;
    private BigDecimal descuento;
    private BigDecimal ice;
    private BigDecimal iva;
    private BigDecimal irbpnr;
    private BigDecimal propina;
    private BigDecimal valorTotal;
    private BigDecimal valorSinSubSidio;
    private BigDecimal ahorroPorSubSidio;
    private List<RespuestaSolicitudSRI> respuestaSolicitudSRI;
    private List<RespuestaSolicitudSRI> respuestaAutorizacionSRI;
    private List<ComprobanteDetalleSRI> detalleFactura;
    private List<ComprobantePagoSRI> pagoDetalle;
    @Expose(deserialize = false, serialize = false)
    private List<InfoAdicional> infoAdicional;

    //NOTA DEBTO - NOTA CREDITO
    private String numComprobanteModifica;
    private String motivoNotaCredito;
    private String fechaEmisionDocumentoModifica;
    private String tipoDocumentoModifica;
    private String descripcionComprobanteModifica;

    ///NOTA DEBITO
    private List<MotivoNotaDebito> motivosNotaDebito;

    ///PARA COMPROBANTE DE RETENCION
    private String periodo;
    private List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion;

    //XML
    private String xmlPath;
    private String pdfPath;

    public ComprobanteSRI() {
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public List<RespuestaSolicitudSRI> getRespuestaSolicitudSRI() {
        return respuestaSolicitudSRI;
    }

    public void setRespuestaSolicitudSRI(List<RespuestaSolicitudSRI> respuestaSolicitudSRI) {
        this.respuestaSolicitudSRI = respuestaSolicitudSRI;
    }

    public List<RespuestaSolicitudSRI> getRespuestaAutorizacionSRI() {
        return respuestaAutorizacionSRI;
    }

    public void setRespuestaAutorizacionSRI(List<RespuestaSolicitudSRI> respuestaAutorizacionSRI) {
        this.respuestaAutorizacionSRI = respuestaAutorizacionSRI;
    }

    public String getNumFacturaFormato() {
        return numFacturaFormato;
    }

    public void setNumFacturaFormato(String numFacturaFormato) {
        this.numFacturaFormato = numFacturaFormato;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getCodigoTipoComprobante() {
        return codigoTipoComprobante;
    }

    public void setCodigoTipoComprobante(String codigoTipoComprobante) {
        this.codigoTipoComprobante = codigoTipoComprobante;
    }

    public ComprobanteEntidadSRI getEntidad() {
        if (entidad == null) {
            entidad = new ComprobanteEntidadSRI();
        }
        return entidad;
    }

    public void setEntidad(ComprobanteEntidadSRI entidad) {
        this.entidad = entidad;
    }

    public ComprobanteEntidadSRI getContribuyente() {
        if (contribuyente == null) {
            contribuyente = new ComprobanteEntidadSRI();
        }
        return contribuyente;
    }

    public void setContribuyente(ComprobanteEntidadSRI contribuyente) {
        this.contribuyente = contribuyente;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public BigDecimal getSubTotal12() {
        return subTotal12;
    }

    public void setSubTotal12(BigDecimal subTotal12) {
        this.subTotal12 = subTotal12;
    }

    public BigDecimal getSubTotalIva() {
        return subTotalIva;
    }

    public void setSubTotalIva(BigDecimal subTotalIva) {
        this.subTotalIva = subTotalIva;
    }

    public BigDecimal getSubTotalNoObjetoIva() {
        return subTotalNoObjetoIva;
    }

    public void setSubTotalNoObjetoIva(BigDecimal subTotalNoObjetoIva) {
        this.subTotalNoObjetoIva = subTotalNoObjetoIva;
    }

    public BigDecimal getSubTotalExcentoIva() {
        return subTotalExcentoIva;
    }

    public void setSubTotalExcentoIva(BigDecimal subTotalExcentoIva) {
        this.subTotalExcentoIva = subTotalExcentoIva;
    }

    public BigDecimal getSubTotalSinImpuetos() {
        return subTotalSinImpuetos;
    }

    public void setSubTotalSinImpuetos(BigDecimal subTotalSinImpuetos) {
        this.subTotalSinImpuetos = subTotalSinImpuetos;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getIce() {
        return ice;
    }

    public void setIce(BigDecimal ice) {
        this.ice = ice;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getIrbpnr() {
        return irbpnr;
    }

    public void setIrbpnr(BigDecimal irbpnr) {
        this.irbpnr = irbpnr;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorSinSubSidio() {
        return valorSinSubSidio;
    }

    public void setValorSinSubSidio(BigDecimal valorSinSubSidio) {
        this.valorSinSubSidio = valorSinSubSidio;
    }

    public BigDecimal getAhorroPorSubSidio() {
        return ahorroPorSubSidio;
    }

    public void setAhorroPorSubSidio(BigDecimal ahorroPorSubSidio) {
        this.ahorroPorSubSidio = ahorroPorSubSidio;
    }

    public List<ComprobanteDetalleSRI> getDetalleFactura() {
        if (detalleFactura == null) {
            detalleFactura = new ArrayList<>();
        }
        return detalleFactura;
    }

    public void setDetalleFactura(List<ComprobanteDetalleSRI> detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public List<ComprobantePagoSRI> getPagoDetalle() {
        if (pagoDetalle == null) {
            pagoDetalle = new ArrayList<>();
        }
        return pagoDetalle;
    }

    public void setPagoDetalle(List<ComprobantePagoSRI> pagoDetalle) {
        this.pagoDetalle = pagoDetalle;
    }

    public List<InfoAdicional> getInfoAdicional() {
        if (infoAdicional == null) {
            infoAdicional = new ArrayList<>();
        }
        return infoAdicional;
    }

    public void setInfoAdicional(List<InfoAdicional> infoAdicional) {
        this.infoAdicional = infoAdicional;
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

    public String getFechaEmisionDocumentoModifica() {
        return fechaEmisionDocumentoModifica;
    }

    public void setFechaEmisionDocumentoModifica(String fechaEmisionDocumentoModifica) {
        this.fechaEmisionDocumentoModifica = fechaEmisionDocumentoModifica;
    }

    public String getTipoDocumentoModifica() {
        return tipoDocumentoModifica;
    }

    public void setTipoDocumentoModifica(String tipoDocumentoModifica) {
        this.tipoDocumentoModifica = tipoDocumentoModifica;
    }

    public String getDescripcionComprobanteModifica() {
        return descripcionComprobanteModifica;
    }

    public void setDescripcionComprobanteModifica(String descripcionComprobanteModifica) {
        this.descripcionComprobanteModifica = descripcionComprobanteModifica;
    }

    public List<MotivoNotaDebito> getMotivosNotaDebito() {
        return motivosNotaDebito;
    }

    public void setMotivosNotaDebito(List<MotivoNotaDebito> motivosNotaDebito) {
        this.motivosNotaDebito = motivosNotaDebito;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public List<ImpuestoComprobanteElectronico> getImpuestoComprobanteRetencion() {
        return impuestoComprobanteRetencion;
    }

    public void setImpuestoComprobanteRetencion(List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion) {
        this.impuestoComprobanteRetencion = impuestoComprobanteRetencion;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    
    
    @Override
    public String toString() {
        return "ComprobanteSRI{" + "xml=" + xmlPath + ", entidad=" + entidad + ", contribuyente=" + contribuyente + ", tipoComprobante=" + tipoComprobante + ", codigoTipoComprobante=" + codigoTipoComprobante + ", numFactura=" + numFactura + ", numFacturaFormato=" + numFacturaFormato + ", numAutorizacion=" + numAutorizacion + ", fechaAutorizacion=" + fechaAutorizacion + ", ambiente=" + ambiente + ", emision=" + emision + ", claveAcceso=" + claveAcceso + ", fechaEmision=" + fechaEmision + ", subTotal12=" + subTotal12 + ", subTotalIva=" + subTotalIva + ", subTotalNoObjetoIva=" + subTotalNoObjetoIva + ", subTotalExcentoIva=" + subTotalExcentoIva + ", subTotalSinImpuetos=" + subTotalSinImpuetos + ", descuento=" + descuento + ", ice=" + ice + ", iva=" + iva + ", irbpnr=" + irbpnr + ", propina=" + propina + ", valorTotal=" + valorTotal + ", valorSinSubSidio=" + valorSinSubSidio + ", ahorroPorSubSidio=" + ahorroPorSubSidio + ", respuestaSolicitudSRI=" + respuestaSolicitudSRI + ", respuestaAutorizacionSRI=" + respuestaAutorizacionSRI + ", detalleFactura=" + detalleFactura + ", pagoDetalle=" + pagoDetalle + '}';
    }

}
