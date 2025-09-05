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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cajero", schema = "facturacion")
public class Cajero implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "entidad", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Entidad entidad;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "punto_emision")
    private String puntoEmision;
    @Column(name = "archivo")
    private String archivo;
    @Column(name = "clave")
    private String clave;
    @Column(name = "variable_secuencia_facturas")
    private String variableSecuenciaFacturas;
    @Column(name = "variable_secuencia_nota_credito")
    private String variableSecuenciaNotaCredito;
    @Column(name = "variable_secuencia_nota_debito")
    private String variableSecuenciaNotaDebito;
    @Column(name = "variable_secuencia_retencion")
    private String variableSecuenciaRetencion;
    @Column(name = "variable_secuencia_guia_remision")
    private String variableSecuenciaGuiaRemision;
    @Column(name = "estado")
    private Boolean estado;

    public Cajero() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getVariableSecuenciaFacturas() {
        return variableSecuenciaFacturas;
    }

    public void setVariableSecuenciaFacturas(String variableSecuenciaFacturas) {
        this.variableSecuenciaFacturas = variableSecuenciaFacturas;
    }

    public String getVariableSecuenciaNotaCredito() {
        return variableSecuenciaNotaCredito;
    }

    public void setVariableSecuenciaNotaCredito(String variableSecuenciaNotaCredito) {
        this.variableSecuenciaNotaCredito = variableSecuenciaNotaCredito;
    }

    public String getVariableSecuenciaNotaDebito() {
        return variableSecuenciaNotaDebito;
    }

    public void setVariableSecuenciaNotaDebito(String variableSecuenciaNotaDebito) {
        this.variableSecuenciaNotaDebito = variableSecuenciaNotaDebito;
    }

    public String getVariableSecuenciaRetencion() {
        return variableSecuenciaRetencion;
    }

    public void setVariableSecuenciaRetencion(String variableSecuenciaRetencion) {
        this.variableSecuenciaRetencion = variableSecuenciaRetencion;
    }

    public String getVariableSecuenciaGuiaRemision() {
        return variableSecuenciaGuiaRemision;
    }

    public void setVariableSecuenciaGuiaRemision(String variableSecuenciaGuiaRemision) {
        this.variableSecuenciaGuiaRemision = variableSecuenciaGuiaRemision;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof Cajero)) {
            return false;
        }
        Cajero other = (Cajero) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.Cajero[ id=" + id + " ]";
    }
    
}
