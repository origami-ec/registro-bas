/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author ANGEL NAVARRO
 */
@Embeddable
public class TbTipoDocPrefPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "cod_usuario", nullable = false)
    private int codUsuario;
    @Basic(optional = false)
    @Column(name = "id_tipo_doc", nullable = false)
    private int idTipoDoc;
    @Basic(optional = false)
    @Column(name = "id_registro", nullable = false)
    private int idRegistro;

    public TbTipoDocPrefPK() {
    }

    public TbTipoDocPrefPK(int codUsuario, int idTipoDoc, int idRegistro) {
        this.codUsuario = codUsuario;
        this.idTipoDoc = idTipoDoc;
        this.idRegistro = idRegistro;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(int idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codUsuario;
        hash += (int) idTipoDoc;
        hash += (int) idRegistro;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocPrefPK)) {
            return false;
        }
        TbTipoDocPrefPK other = (TbTipoDocPrefPK) object;
        if (this.codUsuario != other.codUsuario) {
            return false;
        }
        if (this.idTipoDoc != other.idTipoDoc) {
            return false;
        }
        if (this.idRegistro != other.idRegistro) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.TbTipoDocPrefPK[ codUsuario=" + codUsuario + ", idTipoDoc=" + idTipoDoc + ", idRegistro=" + idRegistro + " ]";
    }

}
