/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.acl.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Fernando
 */
@Entity
@Table(name = "acl_url", schema = "app")
public class AclUrl implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "url_", nullable = false, length = 2000, unique = true)
    private String url;
    
    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL)
    private Collection<AclUrlHasRol> urlHasRolColl;
    
    @Basic(optional = false)
    @Column(name = "sis_enabled", nullable = false)
    private Boolean sisEnabled = true;
    
    
    
    public AclUrl(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Collection<AclUrlHasRol> getUrlHasRolColl() {
        return urlHasRolColl;
    }

    public void setUrlHasRolColl(Collection<AclUrlHasRol> urlHasRolColl) {
        this.urlHasRolColl = urlHasRolColl;
    }

    public Boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AclUrl other = (AclUrl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
