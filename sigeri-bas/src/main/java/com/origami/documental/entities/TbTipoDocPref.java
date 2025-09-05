/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_tipo_doc_pref")
@NamedQueries({
    @NamedQuery(name = "TbTipoDocPref.findAll", query = "SELECT t FROM TbTipoDocPref t")})
public class TbTipoDocPref implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TbTipoDocPrefPK tbTipoDocPrefPK;
    @Column(name = "flg_oculto")
    private Short flgOculto;
    @Column(name = "flg_orden")
    private Short flgOrden;
    @Column(name = "flg_orden_num")
    private Short flgOrdenNum;
    @Column(name = "flg_orden_tipo")
    private Short flgOrdenTipo;
    private Integer width;

    public TbTipoDocPref() {
    }

    public TbTipoDocPref(TbTipoDocPrefPK tbTipoDocPrefPK) {
        this.tbTipoDocPrefPK = tbTipoDocPrefPK;
    }

    public TbTipoDocPref(int codUsuario, int idTipoDoc, int idRegistro) {
        this.tbTipoDocPrefPK = new TbTipoDocPrefPK(codUsuario, idTipoDoc, idRegistro);
    }

    public TbTipoDocPrefPK getTbTipoDocPrefPK() {
        return tbTipoDocPrefPK;
    }

    public void setTbTipoDocPrefPK(TbTipoDocPrefPK tbTipoDocPrefPK) {
        this.tbTipoDocPrefPK = tbTipoDocPrefPK;
    }

    public Short getFlgOculto() {
        return flgOculto;
    }

    public void setFlgOculto(Short flgOculto) {
        this.flgOculto = flgOculto;
    }

    public Short getFlgOrden() {
        return flgOrden;
    }

    public void setFlgOrden(Short flgOrden) {
        this.flgOrden = flgOrden;
    }

    public Short getFlgOrdenNum() {
        return flgOrdenNum;
    }

    public void setFlgOrdenNum(Short flgOrdenNum) {
        this.flgOrdenNum = flgOrdenNum;
    }

    public Short getFlgOrdenTipo() {
        return flgOrdenTipo;
    }

    public void setFlgOrdenTipo(Short flgOrdenTipo) {
        this.flgOrdenTipo = flgOrdenTipo;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tbTipoDocPrefPK != null ? tbTipoDocPrefPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocPref)) {
            return false;
        }
        TbTipoDocPref other = (TbTipoDocPref) object;
        if ((this.tbTipoDocPrefPK == null && other.tbTipoDocPrefPK != null) || (this.tbTipoDocPrefPK != null && !this.tbTipoDocPrefPK.equals(other.tbTipoDocPrefPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.TbTipoDocPref[ tbTipoDocPrefPK=" + tbTipoDocPrefPK + " ]";
    }

}
