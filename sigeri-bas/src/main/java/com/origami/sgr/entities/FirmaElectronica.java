/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "firma_electronica", schema = "administrativo")
public class FirmaElectronica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "funcionario")
    private String funcionario;
    @Column(name = "uid")
    private String uid;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    /*
        Campos FirmaEc
     */
    @Column(name = "estado_firma")
    private String estadoFirma; //Certificado revocado - Certificado caducado  - Certificado emitido por entidad certificadora
    @Column(name = "fecha_emision")
    private Date fechaEmision;
    @Column(name = "fecha_expiracion")
    private Date fechaExpiracion;
    @Column(name = "isuser")
    private String isuser;

    public FirmaElectronica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getIsuser() {
        return isuser;
    }

    public void setIsuser(String isuser) {
        this.isuser = isuser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "FirmaElectronica{" + "id=" + id + ", funcionario=" + funcionario + ", uid=" + uid + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", estadoFirma=" + estadoFirma + ", fechaEmision=" + fechaEmision + ", fechaExpiracion=" + fechaExpiracion + ", isuser=" + isuser + '}';
    }

}
