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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "uaf_int", schema = "ctlg")
@NamedQueries({
    @NamedQuery(name = "UafInt.findAll", query = "SELECT u FROM UafInt u"),
    @NamedQuery(name = "UafInt.findById", query = "SELECT u FROM UafInt u WHERE u.id = :id"),
    @NamedQuery(name = "UafInt.findByPeriodo", query = "SELECT u FROM UafInt u WHERE u.periodo = :periodo"),
    @NamedQuery(name = "UafInt.findByNit", query = "SELECT u FROM UafInt u WHERE u.nit = :nit"),
    @NamedQuery(name = "UafInt.findByIdi", query = "SELECT u FROM UafInt u WHERE u.idi = :idi"),
    @NamedQuery(name = "UafInt.findByNri", query = "SELECT u FROM UafInt u WHERE u.nri = :nri"),
    @NamedQuery(name = "UafInt.findByRdi", query = "SELECT u FROM UafInt u WHERE u.rdi = :rdi"),
    @NamedQuery(name = "UafInt.findByPdi", query = "SELECT u FROM UafInt u WHERE u.pdi = :pdi")})
public class UafInt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "nit")
    private String nit;
    @Column(name = "idi")
    private String idi;
    @Column(name = "nri")
    private String nri;
    @Column(name = "rdi")
    private String rdi;
    @Column(name = "pdi")
    private String pdi;
    @Column(name = "nai")
    private String nai = "ECU";
    @JoinColumn(name = "uaf_tra", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UafTra uafTra;

    @Column(name = "seleccionado")
    private boolean seleccionado;

    @Transient
    private String papel;

    public UafInt() {
        this.seleccionado = false;
    }

    public UafInt(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getIdi() {
        return idi;
    }

    public void setIdi(String idi) {
        this.idi = idi;
    }

    public String getNri() {
        return nri;
    }

    public void setNri(String nri) {
        this.nri = nri;
    }

    public String getRdi() {
        return rdi;
    }

    public void setRdi(String rdi) {
        this.rdi = rdi;
    }

    public String getPdi() {
        return pdi;
    }

    public void setPdi(String pdi) {
        this.pdi = pdi;
    }

    public UafTra getUafTra() {
        return uafTra;
    }

    public void setUafTra(UafTra uafTra) {
        this.uafTra = uafTra;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getNai() {
        return nai;
    }

    public void setNai(String nai) {
        this.nai = nai;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
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
        if (!(object instanceof UafInt)) {
            return false;
        }
        UafInt other = (UafInt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.rpp.entities.UafInt[ id=" + id + " ]";
    }

}
