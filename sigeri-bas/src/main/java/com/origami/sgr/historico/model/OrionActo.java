/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author dfcalderio
 */
public class OrionActo implements Serializable {

    private BigDecimal id;
    private String anio;
    private Integer idActo;
    private String acto;
    private String repertorio;
    private String mes;
    private String cliente;
    private String descripcion;
    private String cuantia;
    private String predio;
    private String parroquia;
    private String integrado;
    private String certificado;
    private String cedUno;
    private String cedDos;
    private Integer idCedUno;
    private Integer idCedDos;

    public OrionActo() {
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Integer getIdActo() {
        return idActo;
    }

    public void setIdActo(Integer idActo) {
        this.idActo = idActo;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public String getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(String repertorio) {
        this.repertorio = repertorio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getIntegrado() {
        return integrado;
    }

    public void setIntegrado(String integrado) {
        this.integrado = integrado;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getCedUno() {
        return cedUno;
    }

    public void setCedUno(String cedUno) {
        this.cedUno = cedUno;
    }

    public String getCedDos() {
        return cedDos;
    }

    public void setCedDos(String cedDos) {
        this.cedDos = cedDos;
    }

    public Integer getIdCedUno() {
        return idCedUno;
    }

    public void setIdCedUno(Integer idCedUno) {
        this.idCedUno = idCedUno;
    }

    public Integer getIdCedDos() {
        return idCedDos;
    }

    public void setIdCedDos(Integer idCedDos) {
        this.idCedDos = idCedDos;
    }

}
