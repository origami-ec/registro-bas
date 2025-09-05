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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "regp_detalle_exoneracion", schema = "flow")
@NamedQueries({
    @NamedQuery(name = "RegpDetalleExoneracion.findAll", query = "SELECT r FROM RegpDetalleExoneracion r"),
    @NamedQuery(name = "RegpDetalleExoneracion.findById", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.id = :id"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByEnte", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.ente = :ente"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByRepresentado", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.representado = :representado"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByObservacion", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.observacion = :observacion"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByEstado", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.estado = :estado"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByUsuarioIngreso", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "RegpDetalleExoneracion.findByFechaIngreso", query = "SELECT r FROM RegpDetalleExoneracion r WHERE r.fechaIngreso = :fechaIngreso")})
public class RegpDetalleExoneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @JoinColumn(name = "representado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte representado;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegpExoneracion exoneracion;
    @JoinColumn(name = "detalle", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegpLiquidacionDetalles detalle;

    public RegpDetalleExoneracion() {
    }

    public RegpDetalleExoneracion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public RegpExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public RegpLiquidacionDetalles getDetalle() {
        return detalle;
    }

    public void setDetalle(RegpLiquidacionDetalles detalle) {
        this.detalle = detalle;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public CatEnte getRepresentado() {
        return representado;
    }

    public void setRepresentado(CatEnte representado) {
        this.representado = representado;
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
        if (!(object instanceof RegpDetalleExoneracion)) {
            return false;
        }
        RegpDetalleExoneracion other = (RegpDetalleExoneracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject4.RegpDetalleExoneracion[ id=" + id + " ]";
    }

}
