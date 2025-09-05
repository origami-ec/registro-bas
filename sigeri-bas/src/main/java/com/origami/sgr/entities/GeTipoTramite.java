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
@Table(name = "ge_tipo_tramite", schema = "app")
@NamedQueries({
    @NamedQuery(name = "GeTipoTramite.findAll", query = "SELECT g FROM GeTipoTramite g")})
public class GeTipoTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activitykey")
    private String activitykey;

    @Column(name = "carpeta")
    private String carpeta;

    @Column(name = "user_direccion")
    private String userDireccion;

    @Column(name = "estado")
    private Boolean estado = true;

    @Column(name = "archivo_bpmn")
    private String archivoBpmn;

    @Column(name = "abreviatura")
    private String abreviatura;

    @Column(name = "tiene_digitalizacion")
    private Boolean tieneDigitalizacion = false;

    @Column(name = "asignacion_manual")
    private Boolean asignacionManual = false;

    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GeDepartamento departamento;

    @JoinColumn(name = "rol", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclRol rol;

    public GeTipoTramite() {
    }

    public GeTipoTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(String userDireccion) {
        this.userDireccion = userDireccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getTieneDigitalizacion() {
        return tieneDigitalizacion;
    }

    public void setTieneDigitalizacion(Boolean tieneDigitalizacion) {
        this.tieneDigitalizacion = tieneDigitalizacion;
    }

    public Boolean getAsignacionManual() {
        return asignacionManual;
    }

    public void setAsignacionManual(Boolean asignacionManual) {
        this.asignacionManual = asignacionManual;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public AclRol getRol() {
        return rol;
    }

    public void setRol(AclRol rol) {
        this.rol = rol;
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
        if (!(object instanceof GeTipoTramite)) {
            return false;
        }
        GeTipoTramite other = (GeTipoTramite) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.GeTipoTramite[ id=" + id + " ]";
    }

}
