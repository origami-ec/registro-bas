/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author EDWIN
 */
@Entity
@Table(name = "reg_requisitos", schema = "app")
public class RegRequisitos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "requisito")
    private String requisito;
    @Column(name = "estado")
    private Boolean estado;
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

    public RegRequisitos() {
    }

    public RegRequisitos(Long id) {
        this.id = id;
    }

    public RegRequisitos(Long id, String requisito) {
        this.id = id;
        this.requisito = requisito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisito() {
        return requisito;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

}
