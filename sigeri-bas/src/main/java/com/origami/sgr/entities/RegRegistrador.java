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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_registrador", schema = "app")
@XmlRootElement
public class RegRegistrador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 500)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Size(max = 500)
    @Column(name = "titulo_completo")
    private String tituloCompleto;
    @Column(name = "actual")
    private boolean actual = false;
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 100)
    @Column(name = "user_ingreso")
    private String userIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 100)
    @Column(name = "user_edicion")
    private String userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "encabezado_reporte")
    private String encabezadoReporte;
    @Column(name = "razon_reporte")
    private String razonReporte;
    @Size(max = 500)
    @Column(name = "file_sign")
    private String fileSign;
    @Size(max = 100)
    @Column(name = "pass_sign")
    private String passSign;
    @Column(name = "firma_juridico")
    private boolean firmaJuridico = false;
    @OneToMany(mappedBy = "registrador", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;
    @Column(name = "digital_sign")
    private String digitalSign;
    @Column(name = "firma_electronica")
    private boolean firmaElectronica = Boolean.FALSE;

    public RegRegistrador() {
    }

    public RegRegistrador(Long id) {
        this.id = id;
    }

    public RegRegistrador(Long id, boolean actual) {
        this.id = id;
        this.actual = actual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreReportes() {
        return nombreCompleto + "\n" + tituloCompleto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTituloCompleto() {
        return tituloCompleto;
    }

    public void setTituloCompleto(String tituloCompleto) {
        this.tituloCompleto = tituloCompleto;
    }

    public boolean getActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(String userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(String userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public String getEncabezadoReporte() {
        return encabezadoReporte;
    }

    public void setEncabezadoReporte(String encabezadoReporte) {
        this.encabezadoReporte = encabezadoReporte;
    }

    public String getRazonReporte() {
        return razonReporte;
    }

    public void setRazonReporte(String razonReporte) {
        this.razonReporte = razonReporte;
    }

    public String getFileSign() {
        return fileSign;
    }

    public void setFileSign(String fileSign) {
        this.fileSign = fileSign;
    }

    public String getPassSign() {
        return passSign;
    }

    public void setPassSign(String passSign) {
        this.passSign = passSign;
    }

    public boolean isFirmaJuridico() {
        return firmaJuridico;
    }

    public void setFirmaJuridico(boolean firmaJuridico) {
        this.firmaJuridico = firmaJuridico;
    }

    @XmlTransient
    public Collection<RegMovimiento> getRegMovimientoCollection() {
        return regMovimientoCollection;
    }

    public void setRegMovimientoCollection(Collection<RegMovimiento> regMovimientoCollection) {
        this.regMovimientoCollection = regMovimientoCollection;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public boolean isFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(boolean firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
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
        if (!(object instanceof RegRegistrador)) {
            return false;
        }
        RegRegistrador other = (RegRegistrador) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegRegistrador[ id=" + id + " ]";
    }

}
