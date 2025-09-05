package com.origami.mail.models;

import java.io.Serializable;

public class CorreoArchivoDto implements Serializable {

    private String nombreArchivo;
    private String tipoArchivo; //pdf - jpg - png
    private String archivoBase64;

    public CorreoArchivoDto() {
    }

    public CorreoArchivoDto(String nombreArchivo, String tipoArchivo, String archivoBase64) {
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo = tipoArchivo;
        this.archivoBase64 = archivoBase64;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }
}
