/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(name = "departamento_usuario", schema = "app")
public class DepartamentoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GeDepartamento departamento;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser usuario;

    @Column(name = "asistente")
    private Boolean asistente;
    @Column(name = "director")
    private Boolean director;
    @Column(name = "jefe")
    private Boolean jefe;
    @Column(name = "liquidador")
    private Boolean liquidador;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public DepartamentoUsuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public Boolean getAsistente() {
        return asistente;
    }

    public void setAsistente(Boolean asistente) {
        this.asistente = asistente;
    }

    public Boolean getDirector() {
        return director;
    }

    public void setDirector(Boolean director) {
        this.director = director;
    }

    public Boolean getJefe() {
        return jefe;
    }

    public void setJefe(Boolean jefe) {
        this.jefe = jefe;
    }

    public Boolean getLiquidador() {
        return liquidador;
    }

    public void setLiquidador(Boolean liquidador) {
        this.liquidador = liquidador;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    
}
