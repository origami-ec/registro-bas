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
@Table(name = "tb_data_intervinientes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_interviniente"})})
@NamedQueries({
    @NamedQuery(name = "TbDataIntervinientes.findAll", query = "SELECT t FROM TbDataIntervinientes t")})
public class TbDataIntervinientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_interviniente", nullable = false)
    private Long id;
    @Column(name = "id_transaccion")
    private Long idTransaccion;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "razon_social")
    private String rasonSocial;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "clase")
    private String clase;

    public TbDataIntervinientes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getRasonSocial() {
        return rasonSocial;
    }

    public void setRasonSocial(String rasonSocial) {
        this.rasonSocial = rasonSocial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
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
        if (!(object instanceof TbData)) {
            return false;
        }
        TbDataIntervinientes other = (TbDataIntervinientes) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TbDataIntervinientes{id=").append(id);
        sb.append(", idTransaccion=").append(idTransaccion);
        sb.append(", identificacion=").append(identificacion);
        sb.append(", nombres=").append(nombres);
        sb.append(", apellidos=").append(apellidos);
        sb.append(", rasonSocial=").append(rasonSocial);
        sb.append(", tipo=").append(tipo);
        sb.append(", clase=").append(clase);
        sb.append('}');
        return sb.toString();
    }

}
