/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_ente_interviniente", schema = "app")
@XmlRootElement
public class RegEnteInterviniente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "ced_ruc")
    private String cedRuc;
    @Column(name = "secuencia")
    private Integer secuencia = 0;
    @Size(max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 1)
    @Column(name = "tipo_interv")
    private String tipoInterv = "N";
    @Size(max = 1)
    @Column(name = "procedencia")
    private String procedencia;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Size(max = 100)
    @Column(name = "usuario_edicion")
    private String usuarioEdicion;
    @JoinColumn(name = "nacionalidad", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UafNacionalidad nacionalidad;
    @OneToMany(mappedBy = "enteInterv", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCliente> regMovimientoClienteCollection;

    @Transient
    private Boolean natural = true;

    public RegEnteInterviniente() {
    }

    public RegEnteInterviniente(Long id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        if (cedRuc != null) {
            switch (cedRuc.trim().length()) {
                case 10:
                    return "C";
                case 13:
                    return "R";
                case 9:
                    return "P";
                default:
                    return "A";
            }
        }
        return "A";
    }

    public String getTipoDocumentoNrpm() {
        if (cedRuc != null) {
            switch (cedRuc.trim().length()) {
                case 10:
                    return "2";
                case 13:
                    return "1";
                default:
                    return "3";
            }
        }
        return "3";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoInterv() {
        return tipoInterv;
    }

    public void setTipoInterv(String tipoInterv) {
        this.tipoInterv = tipoInterv;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public String getUsuarioEdicion() {
        return usuarioEdicion;
    }

    public void setUsuarioEdicion(String usuarioEdicion) {
        this.usuarioEdicion = usuarioEdicion;
    }

    public Boolean getNatural() {
        return natural;
    }

    public void setNatural(Boolean natural) {
        this.natural = natural;
    }

    public UafNacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(UafNacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @XmlTransient
    public Collection<RegMovimientoCliente> getRegMovimientoClienteCollection() {
        return regMovimientoClienteCollection;
    }

    public void setRegMovimientoClienteCollection(Collection<RegMovimientoCliente> regMovimientoClienteCollection) {
        this.regMovimientoClienteCollection = regMovimientoClienteCollection;
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
        if (!(object instanceof RegEnteInterviniente)) {
            return false;
        }
        RegEnteInterviniente other = (RegEnteInterviniente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegEnteInterviniente[ id=" + id + " ]";
    }

}
