/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_certificado_movimiento", schema = "app")
public class RegCertificadoMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "certificado", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegCertificado certificado;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private RegMovimiento movimiento;
    @Column(name = "libro")
    private String libro;
    @Column(name = "acto")
    private String acto;
    @Column(name = "repertorio")
    private Integer repertorio;
    @Column(name = "inscripcion")
    private Integer inscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "folio_inicio")
    private Integer folioInicio;
    @Column(name = "folio_fin")
    private Integer folioFin;
    @Column(name = "tomo")
    private String tomo;
    @Column(name = "notaria")
    private String notaria;
    @Column(name = "canton")
    private String canton;
    @Column(name = "fecha_celebracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCelebracion;
    @Column(name = "juicio_resolucion")
    private String juicioResolucion;
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResolucion;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "cuantia")
    private String cuantia;
    @Column(name = "avaluo")
    private String avaluo;
    @Column(name = "es_negativa")
    private Boolean esNegativa = false;
    @Column(name = "fecha_repertorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimientoCertificado", fetch = FetchType.LAZY)
    private Collection<RegCertificadoMovimientoIntervinientes> regCertificadoMovimientoIntervinientesCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimientoCertificado", fetch = FetchType.LAZY)
    private Collection<RegCertificadoMovimientoReferencias> regCertificadoMovimientoReferenciasCollection;

    public RegCertificadoMovimiento() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegCertificado getCertificado() {
        return certificado;
    }

    public void setCertificado(RegCertificado certificado) {
        this.certificado = certificado;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getFolioInicio() {
        return folioInicio;
    }

    public void setFolioInicio(Integer folioInicio) {
        this.folioInicio = folioInicio;
    }

    public Integer getFolioFin() {
        return folioFin;
    }

    public void setFolioFin(Integer folioFin) {
        this.folioFin = folioFin;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public Date getFechaCelebracion() {
        return fechaCelebracion;
    }

    public void setFechaCelebracion(Date fechaCelebracion) {
        this.fechaCelebracion = fechaCelebracion;
    }

    public String getJuicioResolucion() {
        return juicioResolucion;
    }

    public void setJuicioResolucion(String juicioResolucion) {
        this.juicioResolucion = juicioResolucion;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCuantia() {
        return cuantia;
    }

    public void setCuantia(String cuantia) {
        this.cuantia = cuantia;
    }

    public String getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(String avaluo) {
        this.avaluo = avaluo;
    }

    public Boolean getEsNegativa() {
        return esNegativa;
    }

    public void setEsNegativa(Boolean esNegativa) {
        this.esNegativa = esNegativa;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
    }

    public Collection<RegCertificadoMovimientoIntervinientes> getRegCertificadoMovimientoIntervinientesCollection() {
        return regCertificadoMovimientoIntervinientesCollection;
    }

    public void setRegCertificadoMovimientoIntervinientesCollection(Collection<RegCertificadoMovimientoIntervinientes> regCertificadoMovimientoIntervinientesCollection) {
        this.regCertificadoMovimientoIntervinientesCollection = regCertificadoMovimientoIntervinientesCollection;
    }

    public Collection<RegCertificadoMovimientoReferencias> getRegCertificadoMovimientoReferenciasCollection() {
        return regCertificadoMovimientoReferenciasCollection;
    }

    public void setRegCertificadoMovimientoReferenciasCollection(Collection<RegCertificadoMovimientoReferencias> regCertificadoMovimientoReferenciasCollection) {
        this.regCertificadoMovimientoReferenciasCollection = regCertificadoMovimientoReferenciasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegCertificadoMovimiento)) {
            return false;
        }
        RegCertificadoMovimiento other = (RegCertificadoMovimiento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegCertificadoMovimiento[ id=" + id + " ]";
    }

}
