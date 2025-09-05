/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_parametros_sgr")
@NamedQueries({
    @NamedQuery(name = "TbParametrosSgr.findAll", query = "SELECT t FROM TbParametrosSgr t")})
public class TbParametrosSgr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_parametros", nullable = false)
    private Long idParametros;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String ip;
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(length = 30)
    private String pass;
    @Basic(optional = false)
    @Column(name = "id_carpeta", nullable = false)
    private int idCarpeta;
    @Basic(optional = false)
    @Column(name = "id_libreria", nullable = false)
    private int idLibreria;
    @Column(name = "id_transaccion")
    private Integer idTransaccion;
    @Column(name = "id_blob")
    private Integer idBlob;
    @Column(name = "estado")
    private Integer estado;

    public TbParametrosSgr() {
    }

    public TbParametrosSgr(Long idParametros) {
        this.idParametros = idParametros;
    }

    public TbParametrosSgr(Long idParametros, String ip, int idCarpeta, int idLibreria) {
        this.idParametros = idParametros;
        this.ip = ip;
        this.idCarpeta = idCarpeta;
        this.idLibreria = idLibreria;
    }

    public Long getIdParametros() {
        return idParametros;
    }

    public void setIdParametros(Long idParametros) {
        this.idParametros = idParametros;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIdCarpeta() {
        return idCarpeta;
    }

    public void setIdCarpeta(int idCarpeta) {
        this.idCarpeta = idCarpeta;
    }

    public int getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Integer getIdBlob() {
        return idBlob;
    }

    public void setIdBlob(Integer idBlob) {
        this.idBlob = idBlob;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParametros != null ? idParametros.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbParametrosSgr)) {
            return false;
        }
        TbParametrosSgr other = (TbParametrosSgr) object;
        if ((this.idParametros == null && other.idParametros != null) || (this.idParametros != null && !this.idParametros.equals(other.idParametros))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.TbParametrosSgr[ idParametros=" + idParametros + " ]";
    }

}
