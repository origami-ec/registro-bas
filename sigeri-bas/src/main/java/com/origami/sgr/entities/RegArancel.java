/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "reg_arancel", schema = "app")
@NamedQueries({
    @NamedQuery(name = "RegArancel.findAll", query = "SELECT r FROM RegArancel r ORDER BY r.denominacion"),
    @NamedQuery(name = "RegArancel.findById", query = "SELECT r FROM RegArancel r WHERE r.id = :id"),
    @NamedQuery(name = "RegArancel.findByDenominacion", query = "SELECT r FROM RegArancel r WHERE r.denominacion = :denominacion"),
    @NamedQuery(name = "RegArancel.findByAbreviatura", query = "SELECT r FROM RegArancel r WHERE r.abreviatura = :abreviatura"),
    @NamedQuery(name = "RegArancel.findByCodigo", query = "SELECT r FROM RegArancel r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "RegArancel.findByValorFijo", query = "SELECT r FROM RegArancel r WHERE r.valorFijo = :valorFijo"),
    @NamedQuery(name = "RegArancel.findByValor", query = "SELECT r FROM RegArancel r WHERE r.valor = :valor"),
    @NamedQuery(name = "RegArancel.findByHabilitado", query = "SELECT r FROM RegArancel r WHERE r.habilitado = :habilitado"),
    @NamedQuery(name = "RegArancel.findByUserCre", query = "SELECT r FROM RegArancel r WHERE r.userCre = :userCre"),
    @NamedQuery(name = "RegArancel.findByFechaCre", query = "SELECT r FROM RegArancel r WHERE r.fechaCre = :fechaCre"),
    @NamedQuery(name = "RegArancel.findByUserEdicion", query = "SELECT r FROM RegArancel r WHERE r.userEdicion = :userEdicion"),
    @NamedQuery(name = "RegArancel.findByFechaEdicion", query = "SELECT r FROM RegArancel r WHERE r.fechaEdicion = :fechaEdicion")})
public class RegArancel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(max = 400)
    @Column(name = "denominacion")
    private String denominacion;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "codigo")
    private Integer codigo = 1;
    @Column(name = "valor_fijo")
    private Boolean valorFijo = true;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "habilitado")
    private Boolean habilitado = true;
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

    @OneToMany(mappedBy = "arancel")
    private List<RegActo> actos;

    public RegArancel() {
    }

    public RegArancel(Long id) {
        this.id = id;
    }

    public RegArancel(Long id, String denominacion) {
        this.id = id;
        this.denominacion = denominacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getValorFijo() {
        return valorFijo;
    }

    public void setValorFijo(Boolean valorFijo) {
        this.valorFijo = valorFijo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public List<RegActo> getActos() {
        return actos;
    }

    public void setActos(List<RegActo> actos) {
        this.actos = actos;
    }

    public String getTipoArancel() {
        switch (codigo) {
            case 1:
                return "NORMAL";
            case 2:
                return "50% ARANCEL";
            case 3:
                return "30% ARANCEL";
            default:
                return "NORMAL";
        }
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
        if (!(object instanceof RegArancel)) {
            return false;
        }
        RegArancel other = (RegArancel) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegArancel[ id=" + id + " ]";
    }

    public String valorArancel() {
        if (this.getValor() != null) {
            if (this.valor.compareTo(new BigDecimal("-1")) == 0) {
                return "CUANTIA";
            } else {
                return String.format("%.2f", this.valor.floatValue());
            }
        }

        return "SIN VALOR";
    }
}
