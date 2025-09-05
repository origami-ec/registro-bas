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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author asilva
 */
@Entity
@Table(name = "regp_estado_correccion", schema = "flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegpEstadoCorreccion.findAll", query = "SELECT r FROM RegpEstadoCorreccion r"),
    @NamedQuery(name = "RegpEstadoCorreccion.findById", query = "SELECT r FROM RegpEstadoCorreccion r WHERE r.id = :id"),
    @NamedQuery(name = "RegpEstadoCorreccion.findByCode", query = "SELECT r FROM RegpEstadoCorreccion r WHERE r.code = :code")})
public class RegpEstadoCorreccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "estadoCorreccion")
    private Collection<RegpCorreccion> regpCorreccionCollection;

    public RegpEstadoCorreccion() {
    }

    public RegpEstadoCorreccion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<RegpCorreccion> getRegpCorreccionCollection() {
        return regpCorreccionCollection;
    }

    public void setRegpCorreccionCollection(Collection<RegpCorreccion> regpCorreccionCollection) {
        this.regpCorreccionCollection = regpCorreccionCollection;
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
        if (!(object instanceof RegpEstadoCorreccion)) {
            return false;
        }
        RegpEstadoCorreccion other = (RegpEstadoCorreccion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpEstadoCorreccion[ id=" + id + " ]";
    }
    
}
