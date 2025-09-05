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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ANGEL NAVARRO
 */
@Embeddable
public class TbMarginacionBitPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_marginacion")
    private Integer idMarginacion;
    @Basic(optional = false)
    @Column(name = "fec_transaccion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecTransaccion;

    public TbMarginacionBitPK() {
    }

    public TbMarginacionBitPK(Integer idMarginacion, Date fecTransaccion) {
        this.idMarginacion = idMarginacion;
        this.fecTransaccion = fecTransaccion;
    }

    public Integer getIdMarginacion() {
        return idMarginacion;
    }

    public void setIdMarginacion(Integer idMarginacion) {
        this.idMarginacion = idMarginacion;
    }

    public Date getFecTransaccion() {
        return fecTransaccion;
    }

    public void setFecTransaccion(Date fecTransaccion) {
        this.fecTransaccion = fecTransaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += idMarginacion;
        hash += (fecTransaccion != null ? fecTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbMarginacionBitPK)) {
            return false;
        }
        TbMarginacionBitPK other = (TbMarginacionBitPK) object;
        if (this.idMarginacion != other.idMarginacion) {
            return false;
        }
        if ((this.fecTransaccion == null && other.fecTransaccion != null) || (this.fecTransaccion != null && !this.fecTransaccion.equals(other.fecTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbMarginacionBitPK[ idMarginacion=" + idMarginacion + ", fecTransaccion=" + fecTransaccion + " ]";
    }

}
