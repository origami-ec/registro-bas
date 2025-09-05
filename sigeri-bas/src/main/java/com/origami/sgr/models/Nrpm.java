/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Anyelo
 */
public class Nrpm implements Serializable {

    private Integer tipoparticipanteentrega;
    private Integer tipoidentificacionentrega;
    private String numeroidentificacionentrega;
    private Integer tiporelacionentrega;

    private Integer tipoparticipanterecibe;
    private Integer tipoidentificacionrecibe;
    private String numeroidentificacionrecibe;
    private Integer tiporelacionrecibe;

    private Integer tipotransaccion;
    private Date fechainscripcion;
    private Date fechaescritura;
    private Integer provincia;
    private Integer canton;
    private BigDecimal monto;
    private BigDecimal avaluo;

    public Integer getTipoparticipanteentrega() {
        return tipoparticipanteentrega;
    }

    public void setTipoparticipanteentrega(Integer tipoparticipanteentrega) {
        this.tipoparticipanteentrega = tipoparticipanteentrega;
    }

    public Integer getTipoidentificacionentrega() {
        return tipoidentificacionentrega;
    }

    public void setTipoidentificacionentrega(Integer tipoidentificacionentrega) {
        this.tipoidentificacionentrega = tipoidentificacionentrega;
    }

    public String getNumeroidentificacionentrega() {
        return numeroidentificacionentrega;
    }

    public void setNumeroidentificacionentrega(String numeroidentificacionentrega) {
        this.numeroidentificacionentrega = numeroidentificacionentrega;
    }

    public Integer getTiporelacionentrega() {
        return tiporelacionentrega;
    }

    public void setTiporelacionentrega(Integer tiporelacionentrega) {
        this.tiporelacionentrega = tiporelacionentrega;
    }

    public Integer getTipoparticipanterecibe() {
        return tipoparticipanterecibe;
    }

    public void setTipoparticipanterecibe(Integer tipoparticipanterecibe) {
        this.tipoparticipanterecibe = tipoparticipanterecibe;
    }

    public Integer getTipoidentificacionrecibe() {
        return tipoidentificacionrecibe;
    }

    public void setTipoidentificacionrecibe(Integer tipoidentificacionrecibe) {
        this.tipoidentificacionrecibe = tipoidentificacionrecibe;
    }

    public String getNumeroidentificacionrecibe() {
        return numeroidentificacionrecibe;
    }

    public void setNumeroidentificacionrecibe(String numeroidentificacionrecibe) {
        this.numeroidentificacionrecibe = numeroidentificacionrecibe;
    }

    public Integer getTiporelacionrecibe() {
        return tiporelacionrecibe;
    }

    public void setTiporelacionrecibe(Integer tiporelacionrecibe) {
        this.tiporelacionrecibe = tiporelacionrecibe;
    }

    public Integer getTipotransaccion() {
        return tipotransaccion;
    }

    public void setTipotransaccion(Integer tipotransaccion) {
        this.tipotransaccion = tipotransaccion;
    }

    public Date getFechainscripcion() {
        return fechainscripcion;
    }

    public void setFechainscripcion(Date fechainscripcion) {
        this.fechainscripcion = fechainscripcion;
    }

    public Date getFechaescritura() {
        return fechaescritura;
    }

    public void setFechaescritura(Date fechaescritura) {
        this.fechaescritura = fechaescritura;
    }

    public Integer getProvincia() {
        return provincia;
    }

    public void setProvincia(Integer provincia) {
        this.provincia = provincia;
    }

    public Integer getCanton() {
        return canton;
    }

    public void setCanton(Integer canton) {
        this.canton = canton;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

}
