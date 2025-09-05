package com.origami.sgr.restful.models;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpTareasTramite;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asilva
 */
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
public class DatosProforma implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long numerotramite;
    private Integer repertorio;
    private String doc_solicitante;
    private String nombre_solicitante;
    private String correo_solicitante;
    private String doc_beneficiario;
    private String nombre_beneficiario;
    private String correo_beneficiario;
    private String revisor;
    private String numerofactura;
    private String claveacceso;
    private String numeroautorizacion;
    private String estadotramite;
    private String mensaje;
    private Long fechaingreso;
    private Long fechaentrega;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal dscto_limitcobro;
    private BigDecimal descuento_porc;
    private BigDecimal gastos_generales;
    private BigDecimal totalPagar;
    private String detalleSolicitud;
    private String tareaActual;
    private String fichas;

    private Boolean procedePago;
    private List<DatosDetalleProforma> detalle;

    public DatosProforma() {
    }

    public DatosProforma(HistoricoTramites ht) {
        numerotramite = ht.getNumTramite();
        fechaingreso = ht.getFechaIngreso().getTime();
        //fechaentrega = ht.getFechaEntrega().getTime();
    }

    public DatosProforma(RegpLiquidacion liq) {
        numerotramite = liq.getNumTramiteRp();
        repertorio = liq.getRepertorio();
        doc_solicitante = liq.getSolicitante().getCiRuc();
        nombre_solicitante = liq.getSolicitante().getNombreCompleto();
        correo_solicitante = liq.getSolicitante().getCorreo1();
        if (liq.getBeneficiario() == null) {
            doc_beneficiario = liq.getSolicitante().getCiRuc();
            nombre_beneficiario = liq.getSolicitante().getNombreCompleto();
            correo_beneficiario = liq.getSolicitante().getCorreo1();
        } else {
            doc_beneficiario = liq.getBeneficiario().getCiRuc();
            nombre_beneficiario = liq.getBeneficiario().getNombreCompleto();
            correo_beneficiario = liq.getBeneficiario().getCorreo1();
        }
        if (liq.getInscriptor() != null) {
            revisor = liq.getInscriptor().getCode();
        }
        if (liq.getFechaIngreso() != null) {
            fechaingreso = liq.getFechaIngreso().getTime();
        }
        if (liq.getTramite().getFechaEntrega() != null) {
            fechaentrega = liq.getTramite().getFechaEntrega().getTime();
        }
        subtotal = liq.getSubTotal();
        descuento = liq.getDescuentoValor();
        descuento_porc = liq.getDescuentoPorc();
        dscto_limitcobro = liq.getDescLimitCobro();
        totalPagar = liq.getTotalPagar();
        numerofactura = liq.getCodigoComprobante();
        claveacceso = liq.getClaveAcceso();
        numeroautorizacion = liq.getNumeroAutorizacion();
        DatosDetalleProforma dp;
        detalle = new ArrayList<>();
        for (RegpLiquidacionDetalles det : liq.getRegpLiquidacionDetallesCollection()) {
            dp = new DatosDetalleProforma();
            dp.setActo(det.getActo().getNombre());
            dp.setValorTotal(det.getValorTotal());
            dp.setValorUnitario(det.getValorUnitario());
            dp.setDescuento(det.getDescuento());
            dp.setCuantia(det.getCuantia());
            dp.setAvaluo(det.getAvaluo());
            detalle.add(dp);
        }
    }

    public void constructor(RegpLiquidacion liq) {
        numerotramite = liq.getNumTramiteRp();
        repertorio = liq.getRepertorio();
        doc_solicitante = liq.getSolicitante().getCiRuc();
        nombre_solicitante = liq.getSolicitante().getNombreCompleto();
        correo_solicitante = liq.getSolicitante().getCorreo1();
        if (liq.getBeneficiario() == null) {
            doc_beneficiario = liq.getSolicitante().getCiRuc();
            nombre_beneficiario = liq.getSolicitante().getNombreCompleto();
            correo_beneficiario = liq.getSolicitante().getCorreo1();
        } else {
            doc_beneficiario = liq.getBeneficiario().getCiRuc();
            nombre_beneficiario = liq.getBeneficiario().getNombreCompleto();
            correo_beneficiario = liq.getBeneficiario().getCorreo1();
        }
        if (liq.getInscriptor() != null) {
            if (liq.getInscriptor().getCode() != null) {
                revisor = liq.getInscriptor().getCode();
            } else {
                revisor = "";
            }
        } else {
            revisor = "";
        }

        if (liq.getFechaIngreso() != null) {
            fechaingreso = liq.getFechaIngreso().getTime();
        }
        //fechaentrega = liq.getTramite().getFechaEntrega().getTime();
        subtotal = liq.getSubTotal();
        descuento = liq.getDescuentoValor();
        descuento_porc = liq.getDescuentoPorc();
        dscto_limitcobro = liq.getDescLimitCobro();
        totalPagar = liq.getTotalPagar();
        numerofactura = liq.getCodigoComprobante();
        claveacceso = liq.getClaveAcceso();
        numeroautorizacion = liq.getNumeroAutorizacion();
        DatosDetalleProforma dp;
        detalle = new ArrayList<>();
        for (RegpLiquidacionDetalles det : liq.getRegpLiquidacionDetallesCollection()) {
            dp = new DatosDetalleProforma();
            dp.setActo(det.getActo().getNombre());
            dp.setValorTotal(det.getValorTotal());
            dp.setValorUnitario(det.getValorUnitario());
            dp.setDescuento(det.getDescuento());
            dp.setCuantia(det.getCuantia());
            dp.setAvaluo(det.getAvaluo());
            detalle.add(dp);
        }
        estadotramite = "ETAPA INICIAL";
        if (liq.getTramite().getRegpTareasTramiteCollection() != null) {
            estadotramite = "FINALIZADO";
            for (RegpTareasTramite tt : liq.getTramite().getRegpTareasTramiteCollection()) {
                if (!tt.getRealizado() && !tt.getRevisado()) {
                    estadotramite = "EN PROCESO";
                    return;
                }
            }
        }
    }

    public Long getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(Long numerotramite) {
        this.numerotramite = numerotramite;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public String getDoc_solicitante() {
        return doc_solicitante;
    }

    public void setDoc_solicitante(String doc_solicitante) {
        this.doc_solicitante = doc_solicitante;
    }

    public String getNombre_solicitante() {
        return nombre_solicitante;
    }

    public void setNombre_solicitante(String nombre_solicitante) {
        this.nombre_solicitante = nombre_solicitante;
    }

    public String getCorreo_solicitante() {
        return correo_solicitante;
    }

    public void setCorreo_solicitante(String correo_solicitante) {
        this.correo_solicitante = correo_solicitante;
    }

    public String getDoc_beneficiario() {
        return doc_beneficiario;
    }

    public void setDoc_beneficiario(String doc_beneficiario) {
        this.doc_beneficiario = doc_beneficiario;
    }

    public String getNombre_beneficiario() {
        return nombre_beneficiario;
    }

    public void setNombre_beneficiario(String nombre_beneficiario) {
        this.nombre_beneficiario = nombre_beneficiario;
    }

    public String getCorreo_beneficiario() {
        return correo_beneficiario;
    }

    public void setCorreo_beneficiario(String correo_beneficiario) {
        this.correo_beneficiario = correo_beneficiario;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getNumerofactura() {
        return numerofactura;
    }

    public void setNumerofactura(String numerofactura) {
        this.numerofactura = numerofactura;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public String getEstadotramite() {
        return estadotramite;
    }

    public void setEstadotramite(String estadotramite) {
        this.estadotramite = estadotramite;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Long fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Long getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Long fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getDscto_limitcobro() {
        return dscto_limitcobro;
    }

    public void setDscto_limitcobro(BigDecimal dscto_limitcobro) {
        this.dscto_limitcobro = dscto_limitcobro;
    }

    public BigDecimal getDescuento_porc() {
        return descuento_porc;
    }

    public void setDescuento_porc(BigDecimal descuento_porc) {
        this.descuento_porc = descuento_porc;
    }

    public BigDecimal getGastos_generales() {
        return gastos_generales;
    }

    public void setGastos_generales(BigDecimal gastos_generales) {
        this.gastos_generales = gastos_generales;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public List<DatosDetalleProforma> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DatosDetalleProforma> detalle) {
        this.detalle = detalle;
    }

    public String getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(String detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public String getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(String tareaActual) {
        this.tareaActual = tareaActual;
    }

    public String getFichas() {
        return fichas;
    }

    public void setFichas(String fichas) {
        this.fichas = fichas;
    }

    public Boolean getProcedePago() {
        return procedePago;
    }

    public void setProcedePago(Boolean procedePago) {
        this.procedePago = procedePago;
    }

}
