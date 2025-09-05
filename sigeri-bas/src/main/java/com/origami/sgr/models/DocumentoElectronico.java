/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.util.List;

/**
 *
 * @author Andy
 */
public class DocumentoElectronico {

    private Boolean firmaValida;//validez de todas las firmas
    private Boolean documentoValido;//validez de todo el documento
    private List<Certificado> certificados;//si la lista de certificados se encuentra en null, el documento no contiene firmas
    private String error;

    public DocumentoElectronico() {
    }

    public DocumentoElectronico(Boolean firmaValida, Boolean documentoValido, List<Certificado> certificados, String error) {
        this.firmaValida = firmaValida;
        this.documentoValido = documentoValido;
        this.certificados = certificados;
        this.error = error;
    }

    public Boolean getFirmaValida() {
        return firmaValida;
    }

    public void setFirmaValida(Boolean firmaValida) {
        this.firmaValida = firmaValida;
    }

    public Boolean getDocumentoValido() {
        return documentoValido;
    }

    public void setDocumentoValido(Boolean documentoValido) {
        this.documentoValido = documentoValido;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public String getError() {
        return error;
    }

    public void setProcess(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "\tDocumento\n"
                + "\t[signValidate=" + firmaValida + "\n"
                + "\tdocValidate=" + documentoValido + "\n"
                + "\terror=" + error + "\n"
                + "\t]"
                + "\n----------";
    }
}
