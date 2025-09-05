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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "entidad", schema = "facturacion")
public class Entidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "ruc_entidad")
    private String rucEntidad;
    @Column(name = "nombre_entidad")
    private String nombreEntidad;
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    @Column(name = "establecimiento")
    private String establecimiento;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "mail")
    private String mail;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "direccion_sucursal")
    private String direccionSucursal;
    @Column(name = "logo")
    private String logo;
    @Column(name = "contribuyente_especial")
    private String contribuyenteEspecial;
    @Column(name = "contabilidad")
    private String contabilidad;
    @Column(name = "valor_min")
    private Integer valorMin;
    @Column(name = "valor_max")
    private Integer valorMax;
    @Column(name = "estado")
    private Boolean estado;

    public Entidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRucEntidad() {
        return rucEntidad;
    }

    public void setRucEntidad(String rucEntidad) {
        this.rucEntidad = rucEntidad;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(String contabilidad) {
        this.contabilidad = contabilidad;
    }

    public Integer getValorMin() {
        return valorMin;
    }

    public void setValorMin(Integer valorMin) {
        this.valorMin = valorMin;
    }

    public Integer getValorMax() {
        return valorMax;
    }

    public void setValorMax(Integer valorMax) {
        this.valorMax = valorMax;
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
        if (!(object instanceof Entidad)) {
            return false;
        }
        Entidad other = (Entidad) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.Entidad[ id=" + id + " ]";
    }

}
