/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_tipo_doc_sep")
@NamedQueries({
    @NamedQuery(name = "TbTipoDocSep.findAll", query = "SELECT t FROM TbTipoDocSep t")})
public class TbTipoDocSep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_tipo_doc")
    private Integer idTipoDoc;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_separador", nullable = false)
    private Integer idSeparador;
    @Column(name = "des_separador", length = 100)
    private String desSeparador;

    public TbTipoDocSep() {
    }

    public TbTipoDocSep(Integer idSeparador) {
        this.idSeparador = idSeparador;
    }

    public Integer getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(Integer idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public Integer getIdSeparador() {
        return idSeparador;
    }

    public void setIdSeparador(Integer idSeparador) {
        this.idSeparador = idSeparador;
    }

    public String getDesSeparador() {
        return desSeparador;
    }

    public void setDesSeparador(String desSeparador) {
        this.desSeparador = desSeparador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeparador != null ? idSeparador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocSep)) {
            return false;
        }
        TbTipoDocSep other = (TbTipoDocSep) object;
        if ((this.idSeparador == null && other.idSeparador != null) || (this.idSeparador != null && !this.idSeparador.equals(other.idSeparador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.TbTipoDocSep[ idSeparador=" + idSeparador + " ]";
    }

}
