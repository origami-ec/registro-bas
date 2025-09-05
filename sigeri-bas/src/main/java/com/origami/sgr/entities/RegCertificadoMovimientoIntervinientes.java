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

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_certificado_movimiento_intervinientes", schema = "app")
@NamedQueries({
    @NamedQuery(name = "RegCertificadoMovimientoIntervinientes.findAll", query = "SELECT r FROM RegCertificadoMovimientoIntervinientes r")})
public class RegCertificadoMovimientoIntervinientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "movimiento_certificado", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegCertificadoMovimiento movimientoCertificado;
    @Column(name = "papel")
    private String papel;
    @Column(name = "documento")
    private String documento;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "domicilio")
    private String domicilio;

    public RegCertificadoMovimientoIntervinientes() {
    }

    public RegCertificadoMovimientoIntervinientes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegCertificadoMovimiento getMovimientoCertificado() {
        return movimientoCertificado;
    }

    public void setMovimientoCertificado(RegCertificadoMovimiento movimientoCertificado) {
        this.movimientoCertificado = movimientoCertificado;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
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
        if (!(object instanceof RegCertificadoMovimientoIntervinientes)) {
            return false;
        }
        RegCertificadoMovimientoIntervinientes other = (RegCertificadoMovimientoIntervinientes) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegCertificadoMovimientoIntervinientes[ id=" + id + " ]";
    }
    
}
