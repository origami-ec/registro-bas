package com.origami.sgr.models;

import java.io.Serializable;

public class Pago implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String idServicio;
    private String idInstitucion;
    private String idCanal;
    private String idUsuario;
    private String idDeuda;
    private String secuencial;
    private String valorDeuda;
    private String idPagoIr;
    private String fechaProcesoIr;
    private String fechaTransaccion;
    private String estado;
    private String observacionPago;

    public Pago() {
        super();
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(String valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getIdPagoIr() {
        return idPagoIr;
    }

    public void setIdPagoIr(String idPagoIr) {
        this.idPagoIr = idPagoIr;
    }

    public String getFechaProcesoIr() {
        return fechaProcesoIr;
    }

    public void setFechaProcesoIr(String fechaProcesoIr) {
        this.fechaProcesoIr = fechaProcesoIr;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionPago() {
        return observacionPago;
    }

    public void setObservacionPago(String observacionPago) {
        this.observacionPago = observacionPago;
    }

    @Override
    public String toString() {
        return "Pago [idServicio=" + idServicio + ", idInstitucion=" + idInstitucion + ", idCanal=" + idCanal
                + ", idUsuario=" + idUsuario + ", idDeuda=" + idDeuda + ", secuencial=" + secuencial + ", valorDeuda="
                + valorDeuda + ", idPagoIr=" + idPagoIr + ", fechaProcesoIr=" + fechaProcesoIr + ", fechaTransaccion="
                + fechaTransaccion + "]";
    }

}
