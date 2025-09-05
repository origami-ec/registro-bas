/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "uaf_usuarios", schema = "ctlg")
public class UafUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "acl_user", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser aclUser;
    @Column(name = "codigo_user")
    private String codigoUser;
    @Column(name = "estado")
    private Boolean estado;
    
    public UafUsuarios() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclUser getAclUser() {
        return aclUser;
    }

    public void setAclUser(AclUser aclUser) {
        this.aclUser = aclUser;
    }

    public String getCodigoUser() {
        return codigoUser;
    }

    public void setCodigoUser(String codigoUser) {
        this.codigoUser = codigoUser;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof UafUsuarios)) {
            return false;
        }
        UafUsuarios other = (UafUsuarios) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.UafUsuarios[ id=" + id + " ]";
    }
    
}
