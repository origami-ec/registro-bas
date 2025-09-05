/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "uaf_tramite", schema = "ctlg")
@NamedQueries({
    @NamedQuery(name = "UafTramite.findAll", query = "SELECT u FROM UafTramite u")
    , @NamedQuery(name = "UafTramite.findById", query = "SELECT u FROM UafTramite u WHERE u.id = :id")
    , @NamedQuery(name = "UafTramite.findByCodigo", query = "SELECT u FROM UafTramite u WHERE u.codigo = :codigo")
    , @NamedQuery(name = "UafTramite.findByClaseTramite", query = "SELECT u FROM UafTramite u WHERE u.claseTramite = :claseTramite")
    , @NamedQuery(name = "UafTramite.findByActo", query = "SELECT u FROM UafTramite u WHERE u.acto = :acto")})
public class UafTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 500)
    @Column(name = "clase_tramite")
    private String claseTramite;
    @Column(name = "acto")
    private BigInteger acto;

    public UafTramite() {
    }

    public UafTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClaseTramite() {
        return claseTramite;
    }

    public void setClaseTramite(String claseTramite) {
        this.claseTramite = claseTramite;
    }

    public BigInteger getActo() {
        return acto;
    }

    public void setActo(BigInteger acto) {
        this.acto = acto;
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
        if (!(object instanceof UafTramite)) {
            return false;
        }
        UafTramite other = (UafTramite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.rpm.entities.UafTramite[ id=" + id + " ]";
    }
    
}
