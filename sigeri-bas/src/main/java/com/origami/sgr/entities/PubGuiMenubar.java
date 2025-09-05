/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "pub_gui_menubar", schema = "flow")
@NamedQueries({
    @NamedQuery(name = "PubGuiMenubar.findAll", query = "SELECT p FROM PubGuiMenubar p"),
    @NamedQuery(name = "PubGuiMenubar.findById", query = "SELECT p FROM PubGuiMenubar p WHERE p.id = :id"),
    @NamedQuery(name = "PubGuiMenubar.findByIdentificador", query = "SELECT p FROM PubGuiMenubar p WHERE p.identificador = :identificador")})
public class PubGuiMenubar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "identificador")
    private String identificador;
    @OneToMany(mappedBy = "menubar", fetch = FetchType.LAZY)
    private Collection<PubGuiMenu> pubGuiMenuCollection;

    @Transient
    private List<PubGuiMenu> menuListSoyMenubar_byOrden;
    
    public PubGuiMenubar() {
    }

    public PubGuiMenubar(Integer id) {
        this.id = id;
    }

    public PubGuiMenubar(Integer id, String identificador) {
        this.id = id;
        this.identificador = identificador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Collection<PubGuiMenu> getPubGuiMenuCollection() {
        return pubGuiMenuCollection;
    }

    public void setPubGuiMenuCollection(Collection<PubGuiMenu> pubGuiMenuCollection) {
        this.pubGuiMenuCollection = pubGuiMenuCollection;
    }

    public List<PubGuiMenu> getMenuListSoyMenubar_byOrden() {
        return menuListSoyMenubar_byOrden;
    }

    public void setMenuListSoyMenubar_byOrden(List<PubGuiMenu> menuListSoyMenubar_byOrden) {
        this.menuListSoyMenubar_byOrden = menuListSoyMenubar_byOrden;
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
        if (!(object instanceof PubGuiMenubar)) {
            return false;
        }
        PubGuiMenubar other = (PubGuiMenubar) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenubar[ id=" + id + " ]";
    }
    
}
