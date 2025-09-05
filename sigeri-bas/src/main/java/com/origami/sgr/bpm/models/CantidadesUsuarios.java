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
public class CantidadesUsuarios implements Serializable {

    public static final Long serialVersionUID = 1L;

    protected String user;
    protected String name;
    protected Integer tareas;
    protected Integer fichas;
    protected Integer movimientos;

    public CantidadesUsuarios() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTareas() {
        return tareas;
    }

    public void setTareas(Integer tareas) {
        this.tareas = tareas;
    }

    public Integer getFichas() {
        return fichas;
    }

    public void setFichas(Integer fichas) {
        this.fichas = fichas;
    }

    public Integer getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Integer movimientos) {
        this.movimientos = movimientos;
    }

}
