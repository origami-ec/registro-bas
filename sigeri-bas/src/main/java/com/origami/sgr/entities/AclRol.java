/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "acl_rol", schema = "app")
@NamedQueries({
    @NamedQuery(name = "AclRol.findAll", query = "SELECT a FROM AclRol a"),
    @NamedQuery(name = "AclRol.findById", query = "SELECT a FROM AclRol a WHERE a.id = :id"),
    @NamedQuery(name = "AclRol.findByNombre", query = "SELECT a FROM AclRol a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AclRol.findByEstado", query = "SELECT a FROM AclRol a WHERE a.estado = :estado"),
    @NamedQuery(name = "AclRol.findByIsDirector", query = "SELECT a FROM AclRol a WHERE a.isDirector = :isDirector"),
    @NamedQuery(name = "AclRol.findByEsSubDirector", query = "SELECT a FROM AclRol a WHERE a.esSubDirector = :esSubDirector")})
public class AclRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private boolean estado = true;
    @Column(name = "is_director")
    private Boolean isDirector = false;
    @Column(name = "es_sub_director")
    private Boolean esSubDirector = false;
    @ManyToMany(mappedBy = "aclRolCollection")
    @Where(clause = "sis_enabled")
    @OrderBy("usuario ASC")
    private Collection<AclUser> aclUserCollection;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GeDepartamento departamento;
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private Collection<GeTipoTramite> geTipoTramiteCollection;

    public AclRol() {
    }

    public AclRol(Long id) {
        this.id = id;
    }

    public AclRol(Long id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Boolean getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(Boolean isDirector) {
        this.isDirector = isDirector;
    }

    public Boolean getEsSubDirector() {
        return esSubDirector;
    }

    public void setEsSubDirector(Boolean esSubDirector) {
        this.esSubDirector = esSubDirector;
    }

    public Collection<AclUser> getAclUserCollection() {
        return aclUserCollection;
    }

    public void setAclUserCollection(Collection<AclUser> aclUserCollection) {
        this.aclUserCollection = aclUserCollection;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public Collection<GeTipoTramite> getGeTipoTramiteCollection() {
        return geTipoTramiteCollection;
    }

    public void setGeTipoTramiteCollection(Collection<GeTipoTramite> geTipoTramiteCollection) {
        this.geTipoTramiteCollection = geTipoTramiteCollection;
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
        if (!(object instanceof AclRol)) {
            return false;
        }
        AclRol other = (AclRol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.AclRol[ id=" + id + " ]";
    }

}
