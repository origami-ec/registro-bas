/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "tar_tareas_asignadas", schema = "conf")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarTareasAsignadas.findAll", query = "SELECT t FROM TarTareasAsignadas t"),
    @NamedQuery(name = "TarTareasAsignadas.findById", query = "SELECT t FROM TarTareasAsignadas t WHERE t.id = :id"),
    @NamedQuery(name = "TarTareasAsignadas.findByFecha", query = "SELECT t FROM TarTareasAsignadas t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "TarTareasAsignadas.findByPeso", query = "SELECT t FROM TarTareasAsignadas t WHERE t.peso = :peso"),
    @NamedQuery(name = "TarTareasAsignadas.findByCantidad", query = "SELECT t FROM TarTareasAsignadas t WHERE t.cantidad = :cantidad")})
public class TarTareasAsignadas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "peso")
    private BigInteger peso;
    @Column(name = "cantidad")
    private BigInteger cantidad;
    @JoinColumn(name = "usuario_tareas", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TarUsuarioTareas usuarioTareas;

    public TarTareasAsignadas() {
    }

    public TarTareasAsignadas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public TarUsuarioTareas getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(TarUsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
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
        if (!(object instanceof TarTareasAsignadas)) {
            return false;
        }
        TarTareasAsignadas other = (TarTareasAsignadas) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.TarTareasAsignadas[ id=" + id + " ]";
    }
    
}
