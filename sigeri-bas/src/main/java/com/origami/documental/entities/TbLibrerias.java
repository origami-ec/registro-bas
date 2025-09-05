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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_librerias", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_libreria"})})
@NamedQueries({
    @NamedQuery(name = "TbLibrerias.findAll", query = "SELECT t FROM TbLibrerias t")})
public class TbLibrerias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_libreria", nullable = false)
    private Integer idLibreria;
    @Column(name = "des_referencia", length = 100)
    private String desReferencia;
    @Column(name = "des_libreria", length = 60)
    private String desLibreria;
    @Column(name = "host", length = 100)
    private String host1;
    @Column(name = "des_libreria_blob", length = 60)
    private String desLibreriaBlob;
    @Column(name = "host_blob", length = 100)
    private String hostBlob;
    @Column(name = "flg_activa")
    private Boolean flgActiva;
    @Column(name = "flg_blob")
    private Boolean flgBlob;
    @Column(name = "flg_lipsis")
    private Boolean flgLipsis;
    @Column(name = "flg_increment")
    private Boolean flgIncrement;
    @Column(length = 45)
    private String nivel1;

    public TbLibrerias() {
    }

    public TbLibrerias(Integer idLibreria) {
        this.idLibreria = idLibreria;
    }

    public Integer getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(Integer idLibreria) {
        this.idLibreria = idLibreria;
    }

    public String getDesReferencia() {
        return desReferencia;
    }

    public void setDesReferencia(String desReferencia) {
        this.desReferencia = desReferencia;
    }

    public String getDesLibreria() {
        return desLibreria;
    }

    public void setDesLibreria(String desLibreria) {
        this.desLibreria = desLibreria;
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getDesLibreriaBlob() {
        return desLibreriaBlob;
    }

    public void setDesLibreriaBlob(String desLibreriaBlob) {
        this.desLibreriaBlob = desLibreriaBlob;
    }

    public String getHostBlob() {
        return hostBlob;
    }

    public void setHostBlob(String hostBlob) {
        this.hostBlob = hostBlob;
    }

    public Boolean getFlgActiva() {
        return flgActiva;
    }

    public void setFlgActiva(Boolean flgActiva) {
        this.flgActiva = flgActiva;
    }

    public Boolean getFlgBlob() {
        return flgBlob;
    }

    public void setFlgBlob(Boolean flgBlob) {
        this.flgBlob = flgBlob;
    }

    public Boolean getFlgLipsis() {
        return flgLipsis;
    }

    public void setFlgLipsis(Boolean flgLipsis) {
        this.flgLipsis = flgLipsis;
    }

    public Boolean getFlgIncrement() {
        return flgIncrement;
    }

    public void setFlgIncrement(Boolean flgIncrement) {
        this.flgIncrement = flgIncrement;
    }

    public String getNivel1() {
        return nivel1;
    }

    public void setNivel1(String nivel1) {
        this.nivel1 = nivel1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLibreria != null ? idLibreria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbLibrerias)) {
            return false;
        }
        TbLibrerias other = (TbLibrerias) object;
        if ((this.idLibreria == null && other.idLibreria != null) || (this.idLibreria != null && !this.idLibreria.equals(other.idLibreria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbLibrerias[ idLibreria=" + idLibreria + " ]";
    }
    
}
