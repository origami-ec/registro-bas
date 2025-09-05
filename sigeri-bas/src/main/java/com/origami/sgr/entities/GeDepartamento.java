/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ge_departamento", schema = "app")
@NamedQueries({
    @NamedQuery(name = "GeDepartamento.findAll", query = "SELECT g FROM GeDepartamento g"),
    @NamedQuery(name = "GeDepartamento.findById", query = "SELECT g FROM GeDepartamento g WHERE g.id = :id"),
    @NamedQuery(name = "GeDepartamento.findByNombre", query = "SELECT g FROM GeDepartamento g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GeDepartamento.findByDireccion", query = "SELECT g FROM GeDepartamento g WHERE g.direccion = :direccion"),
    @NamedQuery(name = "GeDepartamento.findByPadre", query = "SELECT g FROM GeDepartamento g WHERE g.padre = :padre"),
    @NamedQuery(name = "GeDepartamento.findByEstado", query = "SELECT g FROM GeDepartamento g WHERE g.estado = :estado")})
public class GeDepartamento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "direccion")
    private Boolean direccion = false;
    @Column(name = "padre")
    private BigInteger padre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado = true;
    @OneToMany(mappedBy = "departamento", fetch = FetchType.LAZY)
    private Collection<AclRol> aclRolCollection;
    @OneToMany(mappedBy = "departamento", fetch = FetchType.LAZY)
    private Collection<GeTipoTramite> geTipoTramiteCollection;

    public GeDepartamento() {
    }

    public GeDepartamento(Long id) {
        this.id = id;
    }

    public GeDepartamento(Long id, boolean estado) {
        this.id = id;
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

    public Boolean getDireccion() {
        return direccion;
    }

    public void setDireccion(Boolean direccion) {
        this.direccion = direccion;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Collection<AclRol> getAclRolCollection() {
        return aclRolCollection;
    }

    public void setAclRolCollection(Collection<AclRol> aclRolCollection) {
        this.aclRolCollection = aclRolCollection;
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
        if (!(object instanceof GeDepartamento)) {
            return false;
        }
        GeDepartamento other = (GeDepartamento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.GeDepartamento[ id=" + id + " ]";
    }

}
