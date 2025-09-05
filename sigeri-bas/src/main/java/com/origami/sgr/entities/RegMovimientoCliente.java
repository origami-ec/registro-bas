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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_movimiento_cliente", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegMovimientoCliente.findAll", query = "SELECT r FROM RegMovimientoCliente r"),
    @NamedQuery(name = "RegMovimientoCliente.findById", query = "SELECT r FROM RegMovimientoCliente r WHERE r.id = :id"),
    @NamedQuery(name = "RegMovimientoCliente.findByCedula", query = "SELECT r FROM RegMovimientoCliente r WHERE r.cedula = :cedula"),
    @NamedQuery(name = "RegMovimientoCliente.findByNombres", query = "SELECT r FROM RegMovimientoCliente r WHERE r.nombres = :nombres"),
    @NamedQuery(name = "RegMovimientoCliente.findByEstado", query = "SELECT r FROM RegMovimientoCliente r WHERE r.estado = :estado")})
public class RegMovimientoCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cedula")
    private String cedula;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "papel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegPapel papel;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegMovimiento movimiento;
    @JoinColumn(name = "ente_interv", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente enteInterv;
    @JoinColumn(name = "domicilio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegDomicilio domicilio;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @JoinColumn(name = "uaf_rol", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UafRolInterv uafRol;
    @JoinColumn(name = "uaf_papel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UafPapelInterv uafPapel;

    @Transient
    private Boolean conyugue = true;
    @Transient
    private Boolean changeEnte = false;
    @Transient
    private CtlgItem item;
    @Transient
    private CtlgCargo cargo;
    @Transient
    private Boolean uafe = false;
      
    public RegMovimientoCliente() {
    }

    public RegMovimientoCliente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public RegPapel getPapel() {
        return papel;
    }

    public void setPapel(RegPapel papel) {
        this.papel = papel;
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

    public RegDomicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(RegDomicilio domicilio) {
        this.domicilio = domicilio;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public Boolean getConyugue() {
        return conyugue;
    }

    public void setConyugue(Boolean conyugue) {
        this.conyugue = conyugue;
    }

    public CtlgItem getItem() {
        return item;
    }

    public void setItem(CtlgItem item) {
        this.item = item;
    }

    public UafRolInterv getUafRol() {
        return uafRol;
    }

    public void setUafRol(UafRolInterv uafRol) {
        this.uafRol = uafRol;
    }

    public UafPapelInterv getUafPapel() {
        return uafPapel;
    }

    public void setUafPapel(UafPapelInterv uafPapel) {
        this.uafPapel = uafPapel;
    }

    public Boolean getUafe() {
        return uafe;
    }

    public void setUafe(Boolean uafe) {
        this.uafe = uafe;
    }

    public Boolean getChangeEnte() {
        return changeEnte;
    }

    public void setChangeEnte(Boolean changeEnte) {
        this.changeEnte = changeEnte;
    }

    public CtlgCargo getCargo() {
        return cargo;
    }

    public void setCargo(CtlgCargo cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof RegMovimientoCliente)) {
            return false;
        }
        RegMovimientoCliente other = (RegMovimientoCliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoCliente[ id=" + id + " ]";
    }

}
