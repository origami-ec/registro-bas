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
 * @author Anyelo
 */
@Entity
@Table(name = "ren_cajero", schema = "financiero")
public class RenCajero implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser usuario;
    @Column(name = "codigo_caja")
    private String codigoCaja; //SIEMPRE TRES CARACTERES
    @Column(name = "ruta_comprobantes_generados")
    private String rutaComprobantesGenerados;
    @Column(name = "habilitado")
    private Boolean habilitado;
    @Column(name = "supervisor")
    private Boolean supervisor;
    @Column(name = "variable_secuencia")
    private String variableSecuencia;
    @Column(name = "variable_secuencia_nota_credito")
    private String variableSecuenciaNotaCredito;
    @Column(name = "ruta_comprobantes_autorizados")
    private String rutaComprobantesAutorizados;
    @Column(name = "ruta_comprobantes_enviados")
    private String rutaComprobantesEnviados;
    @JoinColumn(name = "cajero", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cajero cajero;
    
    public RenCajero() {
    }

    public RenCajero(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getRutaComprobantesGenerados() {
        return rutaComprobantesGenerados;
    }

    public void setRutaComprobantesGenerados(String rutaComprobantesGenerados) {
        this.rutaComprobantesGenerados = rutaComprobantesGenerados;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getVariableSecuencia() {
        return variableSecuencia;
    }

    public void setVariableSecuencia(String variableSecuencia) {
        this.variableSecuencia = variableSecuencia;
    }

    public String getRutaComprobantesAutorizados() {
        return rutaComprobantesAutorizados;
    }

    public void setRutaComprobantesAutorizados(String rutaComprobantesAutorizados) {
        this.rutaComprobantesAutorizados = rutaComprobantesAutorizados;
    }

    public String getRutaComprobantesEnviados() {
        return rutaComprobantesEnviados;
    }

    public void setRutaComprobantesEnviados(String rutaComprobantesEnviados) {
        this.rutaComprobantesEnviados = rutaComprobantesEnviados;
    }

    public String getVariableSecuenciaNotaCredito() {
        return variableSecuenciaNotaCredito;
    }

    public void setVariableSecuenciaNotaCredito(String variableSecuenciaNotaCredito) {
        this.variableSecuenciaNotaCredito = variableSecuenciaNotaCredito;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
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
        if (!(object instanceof RenCajero)) {
            return false;
        }
        RenCajero other = (RenCajero) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RenCajero[ id=" + id + " ]";
    }
}
