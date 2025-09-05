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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_movimiento_referencia", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegMovimientoReferencia.findAll", query = "SELECT r FROM RegMovimientoReferencia r"),
    @NamedQuery(name = "RegMovimientoReferencia.findById", query = "SELECT r FROM RegMovimientoReferencia r WHERE r.id = :id"),
    @NamedQuery(name = "RegMovimientoReferencia.findByMovimiento", query = "SELECT r FROM RegMovimientoReferencia r WHERE r.movimiento = :movimiento")})
public class RegMovimientoReferencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "movimiento")
    private Long movimiento;
    @JoinColumn(name = "movimiento_referencia", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegMovimiento movimientoReferencia;
    @Column(name = "secuencia")
    private Integer secuencia;
    @JoinColumn(name = "libro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegLibro libro;
    @JoinColumn(name = "acto", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegActo acto;
    @Column(name = "inscripcion")
    private Integer inscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "folio")
    private Integer folio;
    @Column(name = "marginacion")
    private String marginacion;
    @Transient
    private String usuario;
    @Transient
    private Long idusuario;

    public RegMovimientoReferencia() {
    }

    public RegMovimientoReferencia(Long id) {
        this.id = id;
    }

    public RegMovimientoReferencia(Long id, Long movimiento) {
        this.id = id;
        this.movimiento = movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Long movimiento) {
        this.movimiento = movimiento;
    }

    public RegMovimiento getMovimientoReferencia() {
        return movimientoReferencia;
    }

    public void setMovimientoReferencia(RegMovimiento movimientoReferencia) {
        this.movimientoReferencia = movimientoReferencia;
    }

    public String getMarginacion() {
        return marginacion;
    }

    public void setMarginacion(String marginacion) {
        this.marginacion = marginacion;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
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

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
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
        if (!(object instanceof RegMovimientoReferencia)) {
            return false;
        }
        RegMovimientoReferencia other = (RegMovimientoReferencia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoReferencia[ id=" + id + " ]";
    }

}
