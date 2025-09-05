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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_movimiento_participante", schema = "ctlg")
@XmlRootElement
public class RegMovimientoParticipante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegMovimiento movimiento;
    @JoinColumn(name = "entrega", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente entrega;
    @Column(name = "tipo_participante_entrega")
    private String tipoParticipanteEntrega;
    @Column(name = "tipo_relacion_entrega")
    private String tipoRelacionEntrega = "7";
    @JoinColumn(name = "recibe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente recibe;
    @Column(name = "tipo_participante_recibe")
    private String tipoParticipanteRecibe;
    @Column(name = "tipo_relacion_recibe")
    private String tipoRelacionRecibe = "7";
    @Transient
    private String tipotransaccion;

    public RegMovimientoParticipante() {
    }

    public RegMovimientoParticipante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegEnteInterviniente getEntrega() {
        return entrega;
    }

    public void setEntrega(RegEnteInterviniente entrega) {
        this.entrega = entrega;
    }

    public RegEnteInterviniente getRecibe() {
        return recibe;
    }

    public void setRecibe(RegEnteInterviniente recibe) {
        this.recibe = recibe;
    }

    public String getTipoParticipanteEntrega() {
        return tipoParticipanteEntrega;
    }

    public void setTipoParticipanteEntrega(String tipoParticipanteEntrega) {
        this.tipoParticipanteEntrega = tipoParticipanteEntrega;
    }

    public String getTipoRelacionEntrega() {
        return tipoRelacionEntrega;
    }

    public void setTipoRelacionEntrega(String tipoRelacionEntrega) {
        this.tipoRelacionEntrega = tipoRelacionEntrega;
    }

    public String getTipoParticipanteRecibe() {
        return tipoParticipanteRecibe;
    }

    public void setTipoParticipanteRecibe(String tipoParticipanteRecibe) {
        this.tipoParticipanteRecibe = tipoParticipanteRecibe;
    }

    public String getTipoRelacionRecibe() {
        return tipoRelacionRecibe;
    }

    public void setTipoRelacionRecibe(String tipoRelacionRecibe) {
        this.tipoRelacionRecibe = tipoRelacionRecibe;
    }

    public String getTipotransaccion() {
        return tipotransaccion;
    }

    public void setTipotransaccion(String tipotransaccion) {
        this.tipotransaccion = tipotransaccion;
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
        if (!(object instanceof RegMovimientoParticipante)) {
            return false;
        }
        RegMovimientoParticipante other = (RegMovimientoParticipante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.rpp.entities.RegMovimientoParticipante[ id=" + id + " ]";
    }
}
