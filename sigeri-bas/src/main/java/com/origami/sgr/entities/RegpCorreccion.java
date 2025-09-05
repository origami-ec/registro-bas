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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author asilva
 */
@Entity
@Table(name = "regp_correccion", schema = "flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegpCorreccion.findAll", query = "SELECT r FROM RegpCorreccion r"),
    @NamedQuery(name = "RegpCorreccion.findById", query = "SELECT r FROM RegpCorreccion r WHERE r.id = :id"),
    @NamedQuery(name = "RegpCorreccion.findByFechaIngreso", query = "SELECT r FROM RegpCorreccion r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RegpCorreccion.findByFechaEntrega", query = "SELECT r FROM RegpCorreccion r WHERE r.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "RegpCorreccion.findByUserCreacion", query = "SELECT r FROM RegpCorreccion r WHERE r.userCreacion = :userCreacion")})
public class RegpCorreccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "user_asignado")
    private String userAsignado;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "correccion")
    private Collection<RegpCorreccionDetalles> regpCorreccionDetallesCollection;
    @JoinColumn(name = "estado_correccion", referencedColumnName = "id")
    @ManyToOne
    private RegpEstadoCorreccion estadoCorreccion;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id")
    @ManyToOne
    private HistoricoTramites idTramite;
    @JoinColumn(name = "user_retira", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte userRetira;
    
    public RegpCorreccion() {
    }

    public RegpCorreccion(Long id) {
        this.id = id;
    }

    public RegpCorreccion(Long id, String userCreacion) {
        this.id = id;
        this.userCreacion = userCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public String getUserAsignado() {
        return userAsignado;
    }

    public void setUserAsignado(String userAsignado) {
        this.userAsignado = userAsignado;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<RegpCorreccionDetalles> getRegpCorreccionDetallesCollection() {
        return regpCorreccionDetallesCollection;
    }

    public void setRegpCorreccionDetallesCollection(Collection<RegpCorreccionDetalles> regpCorreccionDetallesCollection) {
        this.regpCorreccionDetallesCollection = regpCorreccionDetallesCollection;
    }

    public RegpEstadoCorreccion getEstadoCorreccion() {
        return estadoCorreccion;
    }

    public void setEstadoCorreccion(RegpEstadoCorreccion estadoCorreccion) {
        this.estadoCorreccion = estadoCorreccion;
    }

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    public CatEnte getUserRetira() {
        return userRetira;
    }

    public void setUserRetira(CatEnte userRetira) {
        this.userRetira = userRetira;
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
        if (!(object instanceof RegpCorreccion)) {
            return false;
        }
        RegpCorreccion other = (RegpCorreccion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpCorreccion[ id=" + id + " ]";
    }
    
}
