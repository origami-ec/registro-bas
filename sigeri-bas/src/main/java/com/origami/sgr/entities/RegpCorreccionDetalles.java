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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asilva
 */
@Entity
@Table(name = "regp_correccion_detalles", schema = "flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegpCorreccionDetalles.findAll", query = "SELECT r FROM RegpCorreccionDetalles r"),
    @NamedQuery(name = "RegpCorreccionDetalles.findById", query = "SELECT r FROM RegpCorreccionDetalles r WHERE r.id = :id"),
    @NamedQuery(name = "RegpCorreccionDetalles.findByUserIngreso", query = "SELECT r FROM RegpCorreccionDetalles r WHERE r.userIngreso = :userIngreso"),
    @NamedQuery(name = "RegpCorreccionDetalles.findByObservacion", query = "SELECT r FROM RegpCorreccionDetalles r WHERE r.observacion = :observacion"),
    @NamedQuery(name = "RegpCorreccionDetalles.findByFechaElaboracion", query = "SELECT r FROM RegpCorreccionDetalles r WHERE r.fechaElaboracion = :fechaElaboracion")})
public class RegpCorreccionDetalles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "user_ingreso")
    private String userIngreso;        
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaElaboracion;
    @JoinColumn(name = "correccion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegpCorreccion correccion;

    public RegpCorreccionDetalles() {
    }

    public RegpCorreccionDetalles(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(String userIngreso) {
        this.userIngreso = userIngreso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public RegpCorreccion getCorreccion() {
        return correccion;
    }

    public void setCorreccion(RegpCorreccion correccion) {
        this.correccion = correccion;
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
        if (!(object instanceof RegpCorreccionDetalles)) {
            return false;
        }
        RegpCorreccionDetalles other = (RegpCorreccionDetalles) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpCorreccionDetalles[ id=" + id + " ]";
    }
    
}
