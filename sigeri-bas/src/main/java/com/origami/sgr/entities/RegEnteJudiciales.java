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
@Table(name = "reg_ente_judiciales", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegEnteJudiciales.findAll", query = "SELECT r FROM RegEnteJudiciales r"),
    @NamedQuery(name = "RegEnteJudiciales.findById", query = "SELECT r FROM RegEnteJudiciales r WHERE r.id = :id"),
    @NamedQuery(name = "RegEnteJudiciales.findByNombre", query = "SELECT r FROM RegEnteJudiciales r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RegEnteJudiciales.findByAbreviatura", query = "SELECT r FROM RegEnteJudiciales r WHERE r.abreviatura = :abreviatura"),
    @NamedQuery(name = "RegEnteJudiciales.findByEstado", query = "SELECT r FROM RegEnteJudiciales r WHERE r.estado = :estado"),
    @NamedQuery(name = "RegEnteJudiciales.findByUserCreacion", query = "SELECT r FROM RegEnteJudiciales r WHERE r.userCreacion = :userCreacion"),
    @NamedQuery(name = "RegEnteJudiciales.findByFechaCreacion", query = "SELECT r FROM RegEnteJudiciales r WHERE r.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "RegEnteJudiciales.findByUserEdicion", query = "SELECT r FROM RegEnteJudiciales r WHERE r.userEdicion = :userEdicion"),
    @NamedQuery(name = "RegEnteJudiciales.findByFechaEdicion", query = "SELECT r FROM RegEnteJudiciales r WHERE r.fechaEdicion = :fechaEdicion")})
public class RegEnteJudiciales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "sedi")
    private Boolean sedi = false;
    @Size(max = 100)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "user_edicion")
    private String userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @OneToMany(mappedBy = "enteJudicial", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;

    public RegEnteJudiciales() {
    }

    public RegEnteJudiciales(Long id) {
        this.id = id;
    }

    public RegEnteJudiciales(Long id, String nombre) {
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getSedi() {
        return sedi;
    }

    public void setSedi(Boolean sedi) {
        this.sedi = sedi;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(String userEdicion) {
        this.userEdicion = userEdicion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegEnteJudiciales)) {
            return false;
        }
        RegEnteJudiciales other = (RegEnteJudiciales) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegEnteJudiciales[ id=" + id + " ]";
    }
    
}
