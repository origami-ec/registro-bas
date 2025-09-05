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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_carpetas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_carpeta"})})
@NamedQueries({
    @NamedQuery(name = "TbCarpetas.findAll", query = "SELECT t FROM TbCarpetas t")})
public class TbCarpetas implements Serializable {

    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento")
    @ManyToOne(fetch = FetchType.LAZY)
    private TbDepartamentos idDepartamento;
    @JoinColumn(name = "id_tipo_doc", referencedColumnName = "id_tipo_doc")
    @ManyToOne(fetch = FetchType.EAGER)
    private TbTipoDocCab idTipoDoc;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carpeta", nullable = false)
    private Short idCarpeta;
    @Column(name = "des_carpeta", length = 60)
    private String desCarpeta;
    @Column(name = "flg_estado")
    private Short flgEstado;

    public TbCarpetas() {
    }

    public TbCarpetas(Short idCarpeta) {
        this.idCarpeta = idCarpeta;
    }

    public TbDepartamentos getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(TbDepartamentos idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public TbTipoDocCab getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(TbTipoDocCab idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public Short getIdCarpeta() {
        return idCarpeta;
    }

    public void setIdCarpeta(Short idCarpeta) {
        this.idCarpeta = idCarpeta;
    }

    public String getDesCarpeta() {
        return desCarpeta;
    }

    public void setDesCarpeta(String desCarpeta) {
        this.desCarpeta = desCarpeta;
    }

    public Short getFlgEstado() {
        return flgEstado;
    }

    public void setFlgEstado(Short flgEstado) {
        this.flgEstado = flgEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarpeta != null ? idCarpeta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbCarpetas)) {
            return false;
        }
        TbCarpetas other = (TbCarpetas) object;
        return !((this.idCarpeta == null && other.idCarpeta != null) || (this.idCarpeta != null && !this.idCarpeta.equals(other.idCarpeta)));
    }

    @Override
    public String toString() {
        return "TbCarpetas[ idCarpeta=" + idCarpeta + " ]";
    }

}
