/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "reg_acto", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegActo.findAll", query = "SELECT r FROM RegActo r"),
    @NamedQuery(name = "RegActo.findById", query = "SELECT r FROM RegActo r WHERE r.id = :id"),
    @NamedQuery(name = "RegActo.findByNombre", query = "SELECT r FROM RegActo r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RegActo.findByAbreviatura", query = "SELECT r FROM RegActo r WHERE r.abreviatura = :abreviatura"),
    @NamedQuery(name = "RegActo.findByEstado", query = "SELECT r FROM RegActo r WHERE r.estado = :estado"),
    @NamedQuery(name = "RegActo.findByAnexoUnoRegPropiedad", query = "SELECT r FROM RegActo r WHERE r.anexoUnoRegPropiedad = :anexoUnoRegPropiedad"),
    @NamedQuery(name = "RegActo.findByAnexoDosMercantilContrato", query = "SELECT r FROM RegActo r WHERE r.anexoDosMercantilContrato = :anexoDosMercantilContrato"),
    @NamedQuery(name = "RegActo.findByAnexoTresMercatilSocNombramientos", query = "SELECT r FROM RegActo r WHERE r.anexoTresMercatilSocNombramientos = :anexoTresMercatilSocNombramientos"),
    @NamedQuery(name = "RegActo.findByUserCre", query = "SELECT r FROM RegActo r WHERE r.userCre = :userCre"),
    @NamedQuery(name = "RegActo.findByFechaCre", query = "SELECT r FROM RegActo r WHERE r.fechaCre = :fechaCre"),
    @NamedQuery(name = "RegActo.findByUserEdicion", query = "SELECT r FROM RegActo r WHERE r.userEdicion = :userEdicion"),
    @NamedQuery(name = "RegActo.findByFechaEdicion", query = "SELECT r FROM RegActo r WHERE r.fechaEdicion = :fechaEdicion")})
public class RegActo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "anexo_uno_reg_propiedad")
    private Boolean anexoUnoRegPropiedad = false;
    @Column(name = "anexo_dos_mercantil_contrato")
    private Boolean anexoDosMercantilContrato = false;
    @Column(name = "anexo_tres_mercatil_soc_nombramientos")
    private Boolean anexoTresMercatilSocNombramientos = false;
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
    @JoinTable(name = "reg_acto_has_papel", joinColumns = {
        @JoinColumn(name = "acto", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "papel", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<RegPapel> regPapelCollection;
    @JoinTable(name = "reg_actos_has_libros", joinColumns = {
        @JoinColumn(name = "acto", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "libro", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<RegLibro> regLibroCollection;
    @OneToMany(mappedBy = "acto", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;

    @JoinColumn(name = "libro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegLibro libro;

    @Column(name = "fijo")
    private Boolean fijo = true;
    @JoinColumn(name = "tipo_acto", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegTipoActo tipoActo;
    @JoinColumn(name = "tipo_cobro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegTipoCobroActo tipoCobro;

    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "dias")
    private Integer dias;
    @Column(name = "solvencia")
    private Boolean solvencia;
    @Column(name = "nrpm_transaccion")
    private String transaccion;

    @JoinColumn(name = "arancel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegArancel arancel;

    @Column(name = "peso")
    private Integer peso;
    @Column(name = "firmar_certificado")
    private Boolean firmarCertificado = Boolean.FALSE;
    @Column(name = "descripcion_acto")
    private String descripcionActo;
    @Column(name = "codigo_anterior")
    private Integer codigoAnterior;

    public String getNameTruncate() {
        if (Utils.isEmpty(nombre).length() > 30) {
            return Utils.isEmpty(nombre).substring(0, 30).concat("...");
        } else {
            return Utils.isEmpty(nombre);
        }
    }

    public String getNameTruncate2() {
        if (Utils.isEmpty(nombre).length() > 60) {
            return Utils.isEmpty(nombre).substring(0, 60).concat("...");
        } else {
            return Utils.isEmpty(nombre);
        }
    }

    public RegActo() {
    }

    public RegActo(Long id) {
        this.id = id;
    }

    public RegActo(Long id, String nombre) {
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

    public Boolean getAnexoUnoRegPropiedad() {
        return anexoUnoRegPropiedad;
    }

    public void setAnexoUnoRegPropiedad(Boolean anexoUnoRegPropiedad) {
        this.anexoUnoRegPropiedad = anexoUnoRegPropiedad;
    }

    public Boolean getAnexoDosMercantilContrato() {
        return anexoDosMercantilContrato;
    }

    public void setAnexoDosMercantilContrato(Boolean anexoDosMercantilContrato) {
        this.anexoDosMercantilContrato = anexoDosMercantilContrato;
    }

    public Boolean getAnexoTresMercatilSocNombramientos() {
        return anexoTresMercatilSocNombramientos;
    }

    public void setAnexoTresMercatilSocNombramientos(Boolean anexoTresMercatilSocNombramientos) {
        this.anexoTresMercatilSocNombramientos = anexoTresMercatilSocNombramientos;
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

    @XmlTransient
    public Collection<RegPapel> getRegPapelCollection() {
        return regPapelCollection;
    }

    public void setRegPapelCollection(Collection<RegPapel> regPapelCollection) {
        this.regPapelCollection = regPapelCollection;
    }

    @XmlTransient
    public Collection<RegMovimiento> getRegMovimientoCollection() {
        return regMovimientoCollection;
    }

    public void setRegMovimientoCollection(Collection<RegMovimiento> regMovimientoCollection) {
        this.regMovimientoCollection = regMovimientoCollection;
    }

    public Boolean getFijo() {
        return fijo;
    }

    public void setFijo(Boolean fijo) {
        this.fijo = fijo;
    }

    public Collection<RegLibro> getRegLibroCollection() {
        return regLibroCollection;
    }

    public void setRegLibroCollection(Collection<RegLibro> regLibroCollection) {
        this.regLibroCollection = regLibroCollection;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public RegTipoActo getTipoActo() {
        return tipoActo;
    }

    public void setTipoActo(RegTipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Boolean getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(Boolean solvencia) {
        this.solvencia = solvencia;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public RegTipoCobroActo getTipoCobro() {
        return tipoCobro;
    }

    public void setTipoCobro(RegTipoCobroActo tipoCobro) {
        this.tipoCobro = tipoCobro;
    }

    public RegArancel getArancel() {
        return arancel;
    }

    public void setArancel(RegArancel arancel) {
        this.arancel = arancel;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Boolean getFirmarCertificado() {
        return firmarCertificado;
    }

    public void setFirmarCertificado(Boolean firmarCertificado) {
        this.firmarCertificado = firmarCertificado;
    }

    public String getDescripcionActo() {
        return descripcionActo;
    }

    public void setDescripcionActo(String descripcionActo) {
        this.descripcionActo = descripcionActo;
    }

    public Integer getCodigoAnterior() {
        return codigoAnterior;
    }

    public void setCodigoAnterior(Integer codigoAnterior) {
        this.codigoAnterior = codigoAnterior;
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
        if (!(object instanceof RegActo)) {
            return false;
        }
        RegActo other = (RegActo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegActo[ id=" + id + " ]";
    }
}
