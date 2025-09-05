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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_libro", schema = "app")
public class RegLibro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "anexo_uno_reg_propiedad")
    private Boolean anexoUnoRegPropiedad = false;
    @Size(max = 100)
    @Column(name = "user_cre")
    private String userCre;
    @Column(name = "fecha_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCre;
    @Size(max = 100)
    @Column(name = "user_edicion")
    private String userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "nombre_carpeta")
    private String nombreCarpeta;
    @Column(name = "codigo_anterior")
    private Long codigoAnterior;
    @Column(name = "tipo")
    private Integer tipo = 0; 
    @Column(name = "propiedad")
    private Boolean propiedad = true;
    @Column(name = "estado_ficha")
    private Boolean estadoFicha = false;
    @Column(name = "anexo_dos_mercantil")
    private Boolean anexoDosMercantil = false;
    @Column(name = "anexo_tres_mercantil")
    private Boolean anexoTresMercantil = false;
    @Column(name = "anexo_cotad")
    private Boolean anexoCotad = false;
    @Column(name = "gabinete")
    private Integer gabinete = 0;

    @OneToMany(mappedBy = "libro")
    private Collection<RegActo> libro;
    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;
    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private Collection<RegActo> regActoList;

    public RegLibro() {
    }

    public RegLibro(Long id) {
        this.id = id;
    }

    public RegLibro(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAnexoUnoRegPropiedad() {
        return anexoUnoRegPropiedad;
    }

    public void setAnexoUnoRegPropiedad(Boolean anexoUnoRegPropiedad) {
        this.anexoUnoRegPropiedad = anexoUnoRegPropiedad;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
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

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    public Collection<RegActo> getRegActoList() {
        return regActoList;
    }

    public void setRegActoList(Collection<RegActo> regActoList) {
        this.regActoList = regActoList;
    }

    public Collection<RegActo> getLibro() {
        return libro;
    }

    public void setLibro(Collection<RegActo> libro) {
        this.libro = libro;
    }

    @XmlTransient
    public Collection<RegMovimiento> getRegMovimientoCollection() {
        return regMovimientoCollection;
    }

    public void setRegMovimientoCollection(Collection<RegMovimiento> regMovimientoCollection) {
        this.regMovimientoCollection = regMovimientoCollection;
    }

    public Long getCodigoAnterior() {
        return codigoAnterior;
    }

    public void setCodigoAnterior(Long codigoAnterior) {
        this.codigoAnterior = codigoAnterior;
    }

    public Integer getTipo() {
        return tipo;
    }
    
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Boolean getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Boolean propiedad) {
        this.propiedad = propiedad;
    }

    public Boolean getEstadoFicha() {
        return estadoFicha;
    }

    public void setEstadoFicha(Boolean estadoFicha) {
        this.estadoFicha = estadoFicha;
    }

    public Boolean getAnexoDosMercantil() {
        return anexoDosMercantil;
    }

    public void setAnexoDosMercantil(Boolean anexoDosMercantil) {
        this.anexoDosMercantil = anexoDosMercantil;
    }

    public Boolean getAnexoTresMercantil() {
        return anexoTresMercantil;
    }

    public void setAnexoTresMercantil(Boolean anexoTresMercantil) {
        this.anexoTresMercantil = anexoTresMercantil;
    }

    public Boolean getAnexoCotad() {
        return anexoCotad;
    }

    public void setAnexoCotad(Boolean anexoCotad) {
        this.anexoCotad = anexoCotad;
    }

    public Integer getGabinete() {
        return gabinete;
    }

    public void setGabinete(Integer gabinete) {
        this.gabinete = gabinete;
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
        if (!(object instanceof RegLibro)) {
            return false;
        }
        RegLibro other = (RegLibro) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegLibro[ id=" + id + " ]";
    }

}
