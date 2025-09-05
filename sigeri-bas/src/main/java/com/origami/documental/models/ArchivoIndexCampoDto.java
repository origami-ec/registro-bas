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
public class ArchivoIndexCampoDto implements Serializable {
    
    private String tipoDato;
    private String descripcion;
    private Boolean obligatorio;
    private List<String> categorias; 
    private String detalle; 

    public ArchivoIndexCampoDto() {
    }
    
    public ArchivoIndexCampoDto(String tipoDato, String descripcion, List<String> categorias, 
            String detalle, Boolean obligatorio) {
        this.tipoDato = tipoDato;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.detalle = detalle;
        this.obligatorio = obligatorio;
    }

    public ArchivoIndexCampoDto(String detalle) {
        this.detalle = detalle;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArchivoIndexCampoDto{");
        sb.append("tipoDato=").append(tipoDato);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", categorias=").append(categorias);
        sb.append(", detalle=").append(detalle);
        sb.append('}');
        return sb.toString();
    }

}
