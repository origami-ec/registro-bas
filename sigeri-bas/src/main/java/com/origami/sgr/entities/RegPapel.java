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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "reg_papel", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegPapel.findAll", query = "SELECT r FROM RegPapel r"),
    @NamedQuery(name = "RegPapel.findById", query = "SELECT r FROM RegPapel r WHERE r.id = :id"),
    @NamedQuery(name = "RegPapel.findByPapel", query = "SELECT r FROM RegPapel r WHERE r.papel = :papel"),
    @NamedQuery(name = "RegPapel.findByAbreviatura", query = "SELECT r FROM RegPapel r WHERE r.abreviatura = :abreviatura"),
    @NamedQuery(name = "RegPapel.findByEstado", query = "SELECT r FROM RegPapel r WHERE r.estado = :estado"),
    @NamedQuery(name = "RegPapel.findByUserCre", query = "SELECT r FROM RegPapel r WHERE r.userCre = :userCre"),
    @NamedQuery(name = "RegPapel.findByFechaCre", query = "SELECT r FROM RegPapel r WHERE r.fechaCre = :fechaCre"),
    @NamedQuery(name = "RegPapel.findByUserEdicion", query = "SELECT r FROM RegPapel r WHERE r.userEdicion = :userEdicion"),
    @NamedQuery(name = "RegPapel.findByFechaEdicion", query = "SELECT r FROM RegPapel r WHERE r.fechaEdicion = :fechaEdicion"),
    @NamedQuery(name = "RegPapel.findByPropietario", query = "SELECT r FROM RegPapel r WHERE r.propietario = :propietario")})
public class RegPapel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "papel")
    private String papel;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "estado")
    private Boolean estado = true;
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
    @Column(name = "propietario")
    private Boolean propietario;
    @ManyToMany(mappedBy = "regPapelCollection", fetch = FetchType.LAZY)
    private Collection<RegActo> regActoCollection;
    @OneToMany(mappedBy = "papel", fetch = FetchType.LAZY)
    private Collection<RegMovimientoCliente> regMovimientoClienteCollection;
    @Column(name = "codigo_nrpm")
    private String codigoNrpm;

    public RegPapel() {
    }

    public RegPapel(Long id) {
        this.id = id;
    }

    public RegPapel(Long id, String papel) {
        this.id = id;
        this.papel = papel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getNombre() {
        return papel;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Boolean getPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
    }

    @XmlTransient
    public Collection<RegActo> getRegActoCollection() {
        return regActoCollection;
    }

    public void setRegActoCollection(Collection<RegActo> regActoCollection) {
        this.regActoCollection = regActoCollection;
    }

    @XmlTransient
    public Collection<RegMovimientoCliente> getRegMovimientoClienteCollection() {
        return regMovimientoClienteCollection;
    }

    public void setRegMovimientoClienteCollection(Collection<RegMovimientoCliente> regMovimientoClienteCollection) {
        this.regMovimientoClienteCollection = regMovimientoClienteCollection;
    }

    public String getCodigoNrpm() {
        return codigoNrpm;
    }

    public void setCodigoNrpm(String codigoNrpm) {
        this.codigoNrpm = codigoNrpm;
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
        if (!(object instanceof RegPapel)) {
            return false;
        }
        RegPapel other = (RegPapel) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "RegPapel[ id=" + id + ", papel=" + papel + ", codigoNrpm=" + codigoNrpm + " ]";
    }

}
