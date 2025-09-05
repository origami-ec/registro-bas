/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "reg_movimiento_representante",  schema = "app")
public class RegMovimientoRepresentante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "fecha_inicio_cargo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioCargo;
    @Column(name = "fecha_fin_cargo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinCargo;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegMovimiento movimiento;
    @JoinColumn(name = "ente_interv", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente enteInterv;
    @JoinColumn(name = "cargo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgCargo cargo;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @JoinColumn(name = "domicilio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegDomicilio domicilio;
    @JoinColumn(name = "cod_tiempo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem codTiempo;
    @Column(name = "atribucion")
    private Long atribucion;
    @Column(name = "tiempo")
    private Integer tiempo;

    public RegMovimientoRepresentante() {
    }

    public RegMovimientoRepresentante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaInicioCargo() {
        return fechaInicioCargo;
    }

    public void setFechaInicioCargo(Date fechaInicioCargo) {
        this.fechaInicioCargo = fechaInicioCargo;
    }

    public Date getFechaFinCargo() {
        return fechaFinCargo;
    }

    public void setFechaFinCargo(Date fechaFinCargo) {
        this.fechaFinCargo = fechaFinCargo;
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

    public CtlgCargo getCargo() {
        return cargo;
    }

    public void setCargo(CtlgCargo cargo) {
        this.cargo = cargo;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public RegDomicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(RegDomicilio domicilio) {
        this.domicilio = domicilio;
    }

    public CtlgItem getCodTiempo() {
        return codTiempo;
    }

    public void setCodTiempo(CtlgItem codTiempo) {
        this.codTiempo = codTiempo;
    }

    public Long getAtribucion() {
        return atribucion;
    }

    public void setAtribucion(Long atribucion) {
        this.atribucion = atribucion;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
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
        if (!(object instanceof RegMovimientoRepresentante)) {
            return false;
        }
        RegMovimientoRepresentante other = (RegMovimientoRepresentante) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoRepresentante[ id=" + id + " ]";
    }
    
}
