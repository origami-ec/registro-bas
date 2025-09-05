/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dfcalderio
 */
public class ClienteActo implements Serializable {

    private Long pk;
    private String id;
    private String idgrp;
    private String idTras;
    private String cliente;
    private String vendedor;
    private String acto;
    private String inscripcion;
    private String repertorio;
    private String anio;
    private Date fechaInscripcion;
    private String cuantia;
    private String catastro;
    private String tipoCliente;
    private String parroquia;
    private String ubicacionPropiedad;
    private String notaria;
    private Date fechaNotaria;
    private Integer vendido;
    private Integer gravado;
    private String linderos;
    private String observaciones;
    private Integer estadoGravamen;
    private String tipoGravamen;
    private String institucion;
    private String idInstitucion;
    private String numero;
    private String numeroGravamen;

    public ClienteActo() {
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdgrp() {
        return idgrp;
    }

    public void setIdgrp(String idgrp) {
        this.idgrp = idgrp;
    }

    public String getIdTras() {
        return idTras;
    }

    public void setIdTras(String idTras) {
        this.idTras = idTras;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(String repertorio) {
        this.repertorio = repertorio;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Integer getEstadoGravamen() {
        return estadoGravamen;
    }

    public void setEstadoGravamen(Integer estadoGravamen) {
        this.estadoGravamen = estadoGravamen;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getCatastro() {
        return catastro;
    }

    public void setCatastro(String catastro) {
        this.catastro = catastro;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getUbicacionPropiedad() {
        return ubicacionPropiedad;
    }

    public void setUbicacionPropiedad(String ubicacionPropiedad) {
        this.ubicacionPropiedad = ubicacionPropiedad;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public Date getFechaNotaria() {
        return fechaNotaria;
    }

    public void setFechaNotaria(Date fechaNotaria) {
        this.fechaNotaria = fechaNotaria;
    }

    public Integer getVendido() {
        return vendido;
    }

    public void setVendido(Integer vendido) {
        this.vendido = vendido;
    }

    public Integer getGravado() {
        return gravado;
    }

    public void setGravado(Integer gravado) {
        this.gravado = gravado;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoGravamen() {
        return tipoGravamen;
    }

    public void setTipoGravamen(String tipoGravamen) {
        this.tipoGravamen = tipoGravamen;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroGravamen() {
        return numeroGravamen;
    }

    public void setNumeroGravamen(String numeroGravamen) {
        this.numeroGravamen = numeroGravamen;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

}
