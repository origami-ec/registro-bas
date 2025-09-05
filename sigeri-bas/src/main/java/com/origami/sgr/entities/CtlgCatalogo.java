/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ctlg_catalogo", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CtlgCatalogo.findAll", query = "SELECT c FROM CtlgCatalogo c"),
    @NamedQuery(name = "CtlgCatalogo.findById", query = "SELECT c FROM CtlgCatalogo c WHERE c.id = :id"),
    @NamedQuery(name = "CtlgCatalogo.findByNombre", query = "SELECT c FROM CtlgCatalogo c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CtlgCatalogo.findByEstado", query = "SELECT c FROM CtlgCatalogo c WHERE c.estado = :estado")})
public class CtlgCatalogo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado = "A";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogo", fetch = FetchType.LAZY)
    private Collection<CtlgItem> ctlgItemCollection;

    public CtlgCatalogo() {
    }

    public CtlgCatalogo(Long id) {
        this.id = id;
    }

    public CtlgCatalogo(Long id, String nombre) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<CtlgItem> getCtlgItemCollection() {
        return ctlgItemCollection;
    }

    public void setCtlgItemCollection(Collection<CtlgItem> ctlgItemCollection) {
        this.ctlgItemCollection = ctlgItemCollection;
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
        if (!(object instanceof CtlgCatalogo)) {
            return false;
        }
        CtlgCatalogo other = (CtlgCatalogo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CtlgCatalogo[ id=" + id + " ]";
    }
    
}
