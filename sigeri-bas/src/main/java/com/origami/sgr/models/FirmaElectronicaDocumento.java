package com.origami.sgr.models;

import java.util.Date;

public class FirmaElectronicaDocumento {

    private FirmaElectronica firmaElectronica;
    private Date fechaFirmar;
    private Date fechaCreacion;

    public FirmaElectronicaDocumento() {

    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public Date getFechaFirmar() {
        return fechaFirmar;
    }

    public void setFechaFirmar(Date fechaFirmar) {
        this.fechaFirmar = fechaFirmar;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    @Override
    public String toString() {
        return "FirmaElectronicaDocumento{" +
                "firmaElectronica=" + firmaElectronica +
                ", fechaFirmar=" + fechaFirmar +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
