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
import javax.persistence.Table;

/**
 *
 * @author XndySxnchez
 */
@Entity
@Table(name = "nprm_sri_detalle", schema = "ctlg")
public class NprmSriDetalle implements Serializable {

    /**
     * id bigserial PRIMARY KEY NOT NULL, nprm_sri bigint REFERENCES
     * ctlg.nprm_sri(id), movimiento bigint, movimientoParticipante bigint,
     * tipo_transaccion varchar(3)
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "nprm_sri", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private NprmSri nprmSri;
    @JoinColumn(name = "movimiento_participante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RegMovimientoParticipante movimientoParticipante;
    @JoinColumn(name = "tipo_transaccion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private NprmSriCatalogo tipoTransaccion;
    @Column(name = "movimiento")
    private BigInteger movimiento;

    public NprmSriDetalle() {
    }

    public NprmSriDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NprmSri getNprmSri() {
        return nprmSri;
    }

    public void setNprmSri(NprmSri nprmSri) {
        this.nprmSri = nprmSri;
    }

    public RegMovimientoParticipante getMovimientoParticipante() {
        return movimientoParticipante;
    }

    public void setMovimientoParticipante(RegMovimientoParticipante movimientoParticipante) {
        this.movimientoParticipante = movimientoParticipante;
    }

    public NprmSriCatalogo getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(NprmSriCatalogo tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigInteger getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(BigInteger movimiento) {
        this.movimiento = movimiento;
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
        if (!(object instanceof NprmSriDetalle)) {
            return false;
        }
        NprmSriDetalle other = (NprmSriDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NprmSriDetalle{" + "id=" + id + ", nprmSri=" + nprmSri + ", movimientoParticipante=" + movimientoParticipante + ", tipoTransaccion=" + tipoTransaccion + '}';
    }

}
