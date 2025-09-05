/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author asilva
 */
public class MovimientosModificadosIngresados implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected Integer num_inscripcion;
    protected Integer num_repertorio;
    protected Date    fecha_ingreso;
    protected Date    fecha_modificacion;
    protected String  nombre_acto;
    protected String  usuario_creacion;

    public Integer getNum_inscripcion() {
        return num_inscripcion;
    }

    public void setNum_inscripcion(Integer num_inscripcion) {
        this.num_inscripcion = num_inscripcion;
    }

    public Integer getNum_repertorio() {
        return num_repertorio;
    }

    public void setNum_repertorio(Integer num_repertorio) {
        this.num_repertorio = num_repertorio;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public Date getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(Date fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public String getNombre_acto() {
        return nombre_acto;
    }

    public void setNombre_acto(String nombre_acto) {
        this.nombre_acto = nombre_acto;
    }

    public String getUsuario_creacion() {
        return usuario_creacion;
    }

    public void setUsuario_creacion(String usuario_creacion) {
        this.usuario_creacion = usuario_creacion;
    }

}
