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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_domicilio", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegDomicilio.findAll", query = "SELECT r FROM RegDomicilio r"),
    @NamedQuery(name = "RegDomicilio.findById", query = "SELECT r FROM RegDomicilio r WHERE r.id = :id"),
    @NamedQuery(name = "RegDomicilio.findByNombre", query = "SELECT r FROM RegDomicilio r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RegDomicilio.findByEstado", query = "SELECT r FROM RegDomicilio r WHERE r.estado = :estado"),
    @NamedQuery(name = "RegDomicilio.findByUsuarioIngreso", query = "SELECT r FROM RegDomicilio r WHERE r.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "RegDomicilio.findByFechaIngreso", query = "SELECT r FROM RegDomicilio r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RegDomicilio.findByUsuarioEdicion", query = "SELECT r FROM RegDomicilio r WHERE r.usuarioEdicion = :usuarioEdicion"),
    @NamedQuery(name = "RegDomicilio.findByFechaEdicion", query = "SELECT r FROM RegDomicilio r WHERE r.fechaEdicion = :fechaEdicion")})
public class RegDomicilio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado = true;
    @Size(max = 50)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 50)
    @Column(name = "usuario_edicion")
    private String usuarioEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @OneToMany(mappedBy = "domicilio", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;
    @OneToMany(mappedBy = "domicilio", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCliente> regMovimientoClienteCollection;

    public RegDomicilio() {
    }

    public RegDomicilio(Long id) {
        this.id = id;
    }

    public RegDomicilio(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioEdicion() {
        return usuarioEdicion;
    }

    public void setUsuarioEdicion(String usuarioEdicion) {
        this.usuarioEdicion = usuarioEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    @XmlTransient
    public Collection<RegMovimiento> getRegMovimientoCollection() {
        return regMovimientoCollection;
    }

    public void setRegMovimientoCollection(Collection<RegMovimiento> regMovimientoCollection) {
        this.regMovimientoCollection = regMovimientoCollection;
    }

    @XmlTransient
    public Collection<RegMovimientoCliente> getRegMovimientoClienteCollection() {
        return regMovimientoClienteCollection;
    }

    public void setRegMovimientoClienteCollection(Collection<RegMovimientoCliente> regMovimientoClienteCollection) {
        this.regMovimientoClienteCollection = regMovimientoClienteCollection;
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
        if (!(object instanceof RegDomicilio)) {
            return false;
        }
        RegDomicilio other = (RegDomicilio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegDomicilio[ id=" + id + " ]";
    }
    
}
