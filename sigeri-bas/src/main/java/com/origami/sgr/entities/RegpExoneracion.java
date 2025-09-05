/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_exoneracion", schema = "flow")
public class RegpExoneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "concepto")
    private String concepto;
    @Column(name = "valor")
    private BigDecimal valor = BigDecimal.ZERO;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "solicitante")
    private String solicitante;
    @Column(name = "base_legal")
    private String baseLegal;
    @Column(name = "tipo_contrato")
    private String tipoContrato;
    @Column(name = "user_ingreso")
    private Long userIngreso;
    @Column(name = "user_edicion")
    private Long userEdicion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
   // @Basic(optional = false)
    @Column(name = "compartida")
    private Boolean compartida;
    @Transient
    private BigDecimal entero = BigDecimal.ZERO;

    public RegpExoneracion() {
    }

    public String getPorcentaje() {
        return valor.multiply(BigDecimal.valueOf(100)).setScale(2) + "%";
    }

    public String getPorPagar() {
        return BigDecimal.ONE.subtract(valor).multiply(BigDecimal.valueOf(100)).setScale(2) + "%";
    }

    public RegpExoneracion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getBaseLegal() {
        return baseLegal;
    }

    public void setBaseLegal(String baseLegal) {
        this.baseLegal = baseLegal;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Long getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(Long userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Long getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(Long userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public BigDecimal getEntero() {
        return entero;
    }

    public void setEntero(BigDecimal entero) {
        this.entero = entero;
    }

    public Boolean getCompartida() {
        return compartida;
    }

    public void setCompartida(Boolean compartida) {
        this.compartida = compartida;
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
        if (!(object instanceof RegpExoneracion)) {
            return false;
        }
        RegpExoneracion other = (RegpExoneracion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

//    @Override
//    public String toString() {
//        return "com.origami.sgr.entities.RegpExoneracion[ id=" + id + " ]";
//    }

}
