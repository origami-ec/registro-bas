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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_marginacion")
@NamedQueries({
    @NamedQuery(name = "TbMarginacion.findAll", query = "SELECT t FROM TbMarginacion t")})
public class TbMarginacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id_transaccion", nullable = false)
    private long idTransaccion;
    @Basic(optional = false)
    @JoinColumn(name = "id_blob", referencedColumnName = "id_blob", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TbBlob idBlob;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_marginacion", nullable = false)
    private Integer idMarginacion;
    @Lob
    @Column(length = 65535)
    private String descripcion;
    @Column(name = "flg_estado")
    private Short flgEstado;
    @Column(name = "usr_ulttran", length = 20)
    private String usrUlttran;

    public TbMarginacion() {
    }

    public TbMarginacion(Integer idMarginacion) {
        this.idMarginacion = idMarginacion;
    }

    public TbMarginacion(Integer idMarginacion, long idTransaccion, TbBlob idBlob) {
        this.idMarginacion = idMarginacion;
        this.idTransaccion = idTransaccion;
        this.idBlob = idBlob;
    }

    public TbMarginacion(long idTransaccion, TbBlob idBlob) {
        this.idTransaccion = idTransaccion;
        this.idBlob = idBlob;
    }

    public TbMarginacion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public TbBlob getIdBlob() {
        return idBlob;
    }

    public void setIdBlob(TbBlob idBlob) {
        this.idBlob = idBlob;
    }

    public Integer getIdMarginacion() {
        return idMarginacion;
    }

    public void setIdMarginacion(Integer idMarginacion) {
        this.idMarginacion = idMarginacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getFlgEstado() {
        return flgEstado;
    }

    public void setFlgEstado(Short flgEstado) {
        this.flgEstado = flgEstado;
    }

    public String getUsrUlttran() {
        return usrUlttran;
    }

    public void setUsrUlttran(String usrUlttran) {
        this.usrUlttran = usrUlttran;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarginacion != null ? idMarginacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbMarginacion)) {
            return false;
        }
        TbMarginacion other = (TbMarginacion) object;
        if ((this.idMarginacion == null && other.idMarginacion != null) || (this.idMarginacion != null && !this.idMarginacion.equals(other.idMarginacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbMarginacion[ idMarginacion=" + idMarginacion + " ]";
    }

}
