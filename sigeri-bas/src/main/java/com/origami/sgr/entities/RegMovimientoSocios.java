/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Origami
 */
@Entity
@Table(name = "reg_movimiento_socios",  schema = "app")
public class RegMovimientoSocios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad", precision = 14, scale = 2)
    private BigDecimal cantidad;
    @Column(name = "valor", precision = 14, scale = 2)
    private BigDecimal valor;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegMovimiento movimiento;
    @JoinColumn(name = "ente_interv", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente enteInterv;
    @JoinColumn(name = "tipo_participacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgTipoParticipacion tipoParticipacion;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;

    public RegMovimientoSocios() {
    }

    public RegMovimientoSocios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegEnteInterviniente getEnteInterv() {
        return enteInterv;
    }

    public void setEnteInterv(RegEnteInterviniente enteInterv) {
        this.enteInterv = enteInterv;
    }

    public CtlgTipoParticipacion getTipoParticipacion() {
        return tipoParticipacion;
    }

    public void setTipoParticipacion(CtlgTipoParticipacion tipoParticipacion) {
        this.tipoParticipacion = tipoParticipacion;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
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
        if (!(object instanceof RegMovimientoSocios)) {
            return false;
        }
        RegMovimientoSocios other = (RegMovimientoSocios) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoSocios[ id=" + id + " ]";
    }
    
}
