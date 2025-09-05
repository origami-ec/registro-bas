/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_usuarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_usuario"}),
    @UniqueConstraint(columnNames = {"cod_usuario"})})
@NamedQueries({
    @NamedQuery(name = "TbUsuarios.findAll", query = "SELECT t FROM TbUsuarios t")})
public class TbUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod_usuario", nullable = false)
    private Integer codUsuario;
    @Basic(optional = false)
    @Column(name = "id_usuario", nullable = false, length = 15)
    private String idUsuario;
    @Basic(optional = false)
    @Column(nullable = false, length = 60)
    private String nombres;
    @Basic(optional = false)
    @Column(name = "cod_estado", nullable = false)
    private int codEstado;
    @Column(length = 60)
    private String clave;
    @Basic(optional = false)
    @Column(nullable = false)
    private short tipo;
    @Column(name = "flg_exportar")
    private Short flgExportar;
    @Column(name = "flg_imprimir")
    private Short flgImprimir;
    @Column(name = "flg_mail")
    private Short flgMail;
    @Column(name = "flg_logon")
    private Short flgLogon;
    @Column(length = 100)
    private String email;
    @Column(name = "usr_creacion")
    private Integer usrCreacion;
    @Column(name = "fec_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "usr_ultmod")
    private Integer usrUltmod;
    @Column(name = "fec_ultmod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUltmod;
    @Column(name = "fec_ultacceso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUltacceso;

    public TbUsuarios() {
    }

    public TbUsuarios(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public TbUsuarios(Integer codUsuario, String idUsuario, String nombres, int codEstado, short tipo) {
        this.codUsuario = codUsuario;
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.codEstado = codEstado;
        this.tipo = tipo;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(int codEstado) {
        this.codEstado = codEstado;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public Short getFlgExportar() {
        return flgExportar;
    }

    public void setFlgExportar(Short flgExportar) {
        this.flgExportar = flgExportar;
    }

    public Short getFlgImprimir() {
        return flgImprimir;
    }

    public void setFlgImprimir(Short flgImprimir) {
        this.flgImprimir = flgImprimir;
    }

    public Short getFlgMail() {
        return flgMail;
    }

    public void setFlgMail(Short flgMail) {
        this.flgMail = flgMail;
    }

    public Short getFlgLogon() {
        return flgLogon;
    }

    public void setFlgLogon(Short flgLogon) {
        this.flgLogon = flgLogon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUsrCreacion() {
        return usrCreacion;
    }

    public void setUsrCreacion(Integer usrCreacion) {
        this.usrCreacion = usrCreacion;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Integer getUsrUltmod() {
        return usrUltmod;
    }

    public void setUsrUltmod(Integer usrUltmod) {
        this.usrUltmod = usrUltmod;
    }

    public Date getFecUltmod() {
        return fecUltmod;
    }

    public void setFecUltmod(Date fecUltmod) {
        this.fecUltmod = fecUltmod;
    }

    public Date getFecUltacceso() {
        return fecUltacceso;
    }

    public void setFecUltacceso(Date fecUltacceso) {
        this.fecUltacceso = fecUltacceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuario != null ? codUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbUsuarios)) {
            return false;
        }
        TbUsuarios other = (TbUsuarios) object;
        if ((this.codUsuario == null && other.codUsuario != null) || (this.codUsuario != null && !this.codUsuario.equals(other.codUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbUsuarios[ codUsuario=" + codUsuario + " ]";
    }
    
}
