/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.DatosUsuarioFirma;
import java.util.Date;

/**
 *
 * @author EDWIN
 */
public class Certificado {

    private String informacionFirmante;//DInformación del firmante
    private String informacionEntidadCertificadora;//Información de la entidad certificadora
    private Date validoDesde;//certificado digital válido desde
    private Date validoHasta;//certificado digital válido hasta
    private Date generado;//fecha de firmar del documento
    private Date revocado;//fecha de revocado del certificado digital
    private Boolean validado;//validación del certificado en las fecha de vigencia
    private String clavesUso;//llaves de uso
    private DatosUsuarioFirma datosUsuario;
    private Boolean firmaVerificada;//Integridad Firma
    private Date docTimeStamp;//Estampa de tiempo
    private String motivoDocumento;//Razón del documento
    private String localizacionDocumento;//Localización del documento

    public Certificado() {
    }

    public Certificado(String informacionFirmante, String informacionEntidadCertificadora, Date validoDesde, Date validoHasta,
            Date generado, Date revocado, Boolean validado, DatosUsuarioFirma datosUsuario) {
        this.informacionFirmante = informacionFirmante;
        this.informacionEntidadCertificadora = informacionEntidadCertificadora;
        this.validoDesde = validoDesde;
        this.validoHasta = validoHasta;
        this.generado = generado;
        this.revocado = revocado;
        this.validado = validado;
        this.datosUsuario = datosUsuario;
    }

    public String getInformacionFirmante() {
        return informacionFirmante;
    }

    public void setInformacionFirmante(String informacionFirmante) {
        this.informacionFirmante = informacionFirmante;
    }

    public String getInformacionEntidadCertificadora() {
        return informacionEntidadCertificadora;
    }

    public void setInformacionEntidadCertificadora(String informacionEntidadCertificadora) {
        this.informacionEntidadCertificadora = informacionEntidadCertificadora;
    }

    public Date getValidoDesde() {
        return validoDesde;
    }

    public void setValidoDesde(Date validoDesde) {
        this.validoDesde = validoDesde;
    }

    public Date getValidoHasta() {
        return validoHasta;
    }

    public void setValidoHasta(Date validoHasta) {
        this.validoHasta = validoHasta;
    }

    public Date getGenerado() {
        return generado;
    }

    public void setGenerado(Date generado) {
        this.generado = generado;
    }

    public Date getRevocado() {
        return revocado;
    }

    public void setRevocado(Date revocado) {
        this.revocado = revocado;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public String getClavesUso() {
        return clavesUso;
    }

    public void setClavesUso(String clavesUso) {
        this.clavesUso = clavesUso;
    }

    public DatosUsuarioFirma getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuarioFirma datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    public Boolean getFirmaVerificada() {
        return firmaVerificada;
    }

    public void setFirmaVerificada(Boolean firmaVerificada) {
        this.firmaVerificada = firmaVerificada;
    }

    public Date getDocTimeStamp() {
        return docTimeStamp;
    }

    public void setDocTimeStamp(Date docTimeStamp) {
        this.docTimeStamp = docTimeStamp;
    }

    public String getMotivoDocumento() {
        return motivoDocumento;
    }

    public void setMotivoDocumento(String motivoDocumento) {
        this.motivoDocumento = motivoDocumento;
    }

    public String getLocalizacionDocumento() {
        return localizacionDocumento;
    }

    public void setLocalizacionDocumento(String localizacionDocumento) {
        this.localizacionDocumento = localizacionDocumento;
    }
}
