/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_marginacion_bit")
@NamedQueries({
    @NamedQuery(name = "TbMarginacionBit.findAll", query = "SELECT t FROM TbMarginacionBit t")})
public class TbMarginacionBit implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TbMarginacionBitPK tbMarginacionBitPK;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "usr_transaccion")
    private String usrTransaccion;
    @Basic(optional = false)
    @Column(name = "tip_transaccion")
    private short tipTransaccion;

    public TbMarginacionBit() {
    }

    public TbMarginacionBit(TbMarginacionBitPK tbMarginacionBitPK) {
        this.tbMarginacionBitPK = tbMarginacionBitPK;
    }

    public TbMarginacionBit(TbMarginacionBitPK tbMarginacionBitPK, String usrTransaccion, short tipTransaccion) {
        this.tbMarginacionBitPK = tbMarginacionBitPK;
        this.usrTransaccion = usrTransaccion;
        this.tipTransaccion = tipTransaccion;
    }

    public TbMarginacionBit(Integer idMarginacion, Date fecTransaccion) {
        this.tbMarginacionBitPK = new TbMarginacionBitPK(idMarginacion, fecTransaccion);
    }

    public TbMarginacionBitPK getTbMarginacionBitPK() {
        return tbMarginacionBitPK;
    }

    public void setTbMarginacionBitPK(TbMarginacionBitPK tbMarginacionBitPK) {
        this.tbMarginacionBitPK = tbMarginacionBitPK;
    }

    public String getUsrTransaccion() {
        return usrTransaccion;
    }

    public void setUsrTransaccion(String usrTransaccion) {
        this.usrTransaccion = usrTransaccion;
    }

    public short getTipTransaccion() {
        return tipTransaccion;
    }

    public void setTipTransaccion(short tipTransaccion) {
        this.tipTransaccion = tipTransaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tbMarginacionBitPK != null ? tbMarginacionBitPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbMarginacionBit)) {
            return false;
        }
        TbMarginacionBit other = (TbMarginacionBit) object;
        if ((this.tbMarginacionBitPK == null && other.tbMarginacionBitPK != null) || (this.tbMarginacionBitPK != null && !this.tbMarginacionBitPK.equals(other.tbMarginacionBitPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.documental.entities.TbMarginacionBit[ tbMarginacionBitPK=" + tbMarginacionBitPK + " ]";
    }

}
