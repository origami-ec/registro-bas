/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduar
 */
public class ArchivoIndexDto implements Serializable {

    private String tramite;
    private String numTramite;
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private String formatoUpload;
    private List<ArchivoIndexCampoDto> detalles;

    public ArchivoIndexDto() {
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getTipoIndexacion() {
        return tipoIndexacion;
    }

    public void setTipoIndexacion(String tipoIndexacion) {
        this.tipoIndexacion = tipoIndexacion;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getFormatoUpload() {
        return formatoUpload;
    }

    public void setFormatoUpload(String formatoUpload) {
        this.formatoUpload = formatoUpload;
    }

    public List<ArchivoIndexCampoDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ArchivoIndexCampoDto> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArchivoIndexDto{");
        sb.append("tramite=").append(tramite);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", tipoIndexacion=").append(tipoIndexacion);
        sb.append(", detalleDocumento=").append(detalleDocumento);
        sb.append(", estado=").append(estado);
        sb.append(", formatoUpload=").append(formatoUpload);
        sb.append(", detalles=").append(detalles);
        sb.append('}');
        return sb.toString();
    }

}
