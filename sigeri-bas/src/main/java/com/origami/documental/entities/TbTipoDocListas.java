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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "tb_tipo_doc_listas")
@NamedQueries({
    @NamedQuery(name = "TbTipoDocListas.findAll", query = "SELECT t FROM TbTipoDocListas t")})
public class TbTipoDocListas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_lista", nullable = false)
    private Integer idLista;
    @Column(name = "id_registro")
    private Integer idRegistro;
    @Lob
    @Column(length = 65535)
    private String etiqueta;
    @Lob
    @Column(length = 65535)
    private String valor;
    @Column(name = "ord_salida")
    private Short ordSalida;
    @Column(name = "flg_default")
    private Short flgDefault;

    public TbTipoDocListas() {
    }

    public TbTipoDocListas(Integer idLista) {
        this.idLista = idLista;
    }

    public Integer getIdLista() {
        return idLista;
    }

    public void setIdLista(Integer idLista) {
        this.idLista = idLista;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Short getOrdSalida() {
        return ordSalida;
    }

    public void setOrdSalida(Short ordSalida) {
        this.ordSalida = ordSalida;
    }

    public Short getFlgDefault() {
        return flgDefault;
    }

    public void setFlgDefault(Short flgDefault) {
        this.flgDefault = flgDefault;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLista != null ? idLista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbTipoDocListas)) {
            return false;
        }
        TbTipoDocListas other = (TbTipoDocListas) object;
        if ((this.idLista == null && other.idLista != null) || (this.idLista != null && !this.idLista.equals(other.idLista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.TbTipoDocListas[ idLista=" + idLista + " ]";
    }

}
