/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.models;

import java.io.Serializable;

/**
 *
 * @author Anyelo
 */
public class DataModel implements Serializable {
    public static final Long serialVersionUID = 1L;
    
    protected Long id;
    protected String nombre = "";
    protected Integer cantidad1 = 0;
    protected Integer cantidad2 = 0;

    public DataModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(Integer cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public Integer getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(Integer cantidad2) {
        this.cantidad2 = cantidad2;
    }

}
