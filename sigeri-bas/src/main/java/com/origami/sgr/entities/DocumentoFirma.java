/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import groovy.transform.Trait;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author EDWIN
 */
@Entity
@Table(name = "documento_firma", schema = "flow")
public class DocumentoFirma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "referencia", nullable = false)
    private Long referencia;
    @Column(name = "num_tramite", nullable = false)
    private Long numTramite;
    @Column(name = "documento", nullable = false)
    private Long documento;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "posicion_x", nullable = false)
    private Integer posicionX1;
    @Column(name = "posicion_y", nullable = false)
    private Integer posicionY1;

    @Column(name = "numero_pagina", nullable = false)
    private Integer numeroPagina;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CtlgItem tipo;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CtlgItem estado;
    @Transient
    private String archivoFirmado;
    @Column(name = "fecha_firma")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFirma;

    public DocumentoFirma() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public CtlgItem getTipo() {
        return tipo;
    }

    public void setTipo(CtlgItem tipo) {
        this.tipo = tipo;
    }

    public CtlgItem getEstado() {
        return estado;
    }

    public void setEstado(CtlgItem estado) {
        this.estado = estado;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Integer getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(Integer posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public Integer getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(Integer posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public String getArchivoFirmado() {
        return archivoFirmado;
    }

    public void setArchivoFirmado(String archivoFirmado) {
        this.archivoFirmado = archivoFirmado;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

}
