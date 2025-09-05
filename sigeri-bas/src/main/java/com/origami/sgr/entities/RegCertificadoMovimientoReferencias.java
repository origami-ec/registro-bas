/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_certificado_movimiento_referencias", schema = "app")
@NamedQueries({
    @NamedQuery(name = "RegCertificadoMovimientoReferencias.findAll", query = "SELECT r FROM RegCertificadoMovimientoReferencias r")})
public class RegCertificadoMovimientoReferencias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "movimiento_certificado", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegCertificadoMovimiento movimientoCertificado;
    @Column(name = "libro_referencia")
    private String libroReferencia;
    @Column(name = "acto_referencia")
    private String actoReferencia;
    @Column(name = "repertorio_referencia")
    private Integer repertorioReferencia;
    @Column(name = "inscripcion_referencia")
    private Integer inscripcionReferencia;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "fecha_repertorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;

    public RegCertificadoMovimientoReferencias() {
    }

    public RegCertificadoMovimientoReferencias(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegCertificadoMovimiento getMovimientoCertificado() {
        return movimientoCertificado;
    }

    public void setMovimientoCertificado(RegCertificadoMovimiento movimientoCertificado) {
        this.movimientoCertificado = movimientoCertificado;
    }

    public String getLibroReferencia() {
        return libroReferencia;
    }

    public void setLibroReferencia(String libroReferencia) {
        this.libroReferencia = libroReferencia;
    }

    public String getActoReferencia() {
        return actoReferencia;
    }

    public void setActoReferencia(String actoReferencia) {
        this.actoReferencia = actoReferencia;
    }

    public Integer getRepertorioReferencia() {
        return repertorioReferencia;
    }

    public void setRepertorioReferencia(Integer repertorioReferencia) {
        this.repertorioReferencia = repertorioReferencia;
    }

    public Integer getInscripcionReferencia() {
        return inscripcionReferencia;
    }

    public void setInscripcionReferencia(Integer inscripcionReferencia) {
        this.inscripcionReferencia = inscripcionReferencia;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
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
        if (!(object instanceof RegCertificadoMovimientoReferencias)) {
            return false;
        }
        RegCertificadoMovimientoReferencias other = (RegCertificadoMovimientoReferencias) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegCertificadoMovimientoReferencias[ id=" + id + " ]";
    }
    
}
