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
@Table(name = "tb_departamentos")
@NamedQueries({
    @NamedQuery(name = "TbDepartamentos.findAll", query = "SELECT t FROM TbDepartamentos t")})
public class TbDepartamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_departamento", nullable = false)
    private Integer idDepartamento;
    @Column(name = "des_departamento", length = 60)
    private String desDepartamento;
    @Column(name = "id_libreria")
    private Integer idLibreria;

    public TbDepartamentos() {
    }

    public TbDepartamentos(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getDesDepartamento() {
        return desDepartamento;
    }

    public void setDesDepartamento(String desDepartamento) {
        this.desDepartamento = desDepartamento;
    }

    public Integer getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(Integer idLibreria) {
        this.idLibreria = idLibreria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDepartamento != null ? idDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbDepartamentos)) {
            return false;
        }
        TbDepartamentos other = (TbDepartamentos) object;
        if ((this.idDepartamento == null && other.idDepartamento != null) || (this.idDepartamento != null && !this.idDepartamento.equals(other.idDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TbDepartamentos[ idDepartamento=" + idDepartamento + " ]";
    }
    
}
