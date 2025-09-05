/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.model;

import java.io.Serializable;

/**
 *
 * @author dfcalderio
 */
public class Cliente implements Serializable {

    private String id;
    private String idTras;
    private String idGrp;
    private String cedula;
    private String nombre;
    private String tipo;

    public Cliente() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTras() {
        return idTras;
    }

    public void setIdTras(String idTras) {
        this.idTras = idTras;
    }

    public String getIdGrp() {
        return idGrp;
    }

    public void setIdGrp(String idGrp) {
        this.idGrp = idGrp;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
