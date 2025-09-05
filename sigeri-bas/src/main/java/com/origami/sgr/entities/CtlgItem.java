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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "ctlg_item", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CtlgItem.findAll", query = "SELECT c FROM CtlgItem c"),
    @NamedQuery(name = "CtlgItem.findById", query = "SELECT c FROM CtlgItem c WHERE c.id = :id"),
    @NamedQuery(name = "CtlgItem.findByValor", query = "SELECT c FROM CtlgItem c WHERE c.valor = :valor"),
    @NamedQuery(name = "CtlgItem.findByCodename", query = "SELECT c FROM CtlgItem c WHERE c.codename = :codename"),
    @NamedQuery(name = "CtlgItem.findByReferencia", query = "SELECT c FROM CtlgItem c WHERE c.referencia = :referencia"),
    @NamedQuery(name = "CtlgItem.findByEstado", query = "SELECT c FROM CtlgItem c WHERE c.estado = :estado")})
public class CtlgItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 80)
    @Column(name = "valor")
    private String valor;
    @Size(max = 40)
    @Column(name = "codename")
    private String codename;
    @Column(name = "referencia")
    private BigInteger referencia;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado = "A";
    @JoinColumn(name = "catalogo", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CtlgCatalogo catalogo;

    public CtlgItem() {
    }

    public CtlgItem(Long id) {
        this.id = id;
    }

    public CtlgItem(Long id, String valor, String codename) {
        this.id = id;
        this.valor = valor;
        this.codename = codename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public BigInteger getReferencia() {
        return referencia;
    }

    public void setReferencia(BigInteger referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
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
        if (!(object instanceof CtlgItem)) {
            return false;
        }
        CtlgItem other = (CtlgItem) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CtlgItem[ id=" + id + " ]";
    }
    
}
