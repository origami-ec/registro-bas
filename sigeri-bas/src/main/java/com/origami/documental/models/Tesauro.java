/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ORIGAMI
 */
public class Tesauro {

    public String _id;
    private String palabra;
    private Boolean estado;
    private Date fecha;
    private List<String> sugerencias;
    private UsuarioDocs usuario;
    private UsuarioDocs usuarioEdita;

    public Tesauro() {
    }

    public Tesauro(String palabra) {
        this.palabra = palabra;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public List<String> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<String> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public UsuarioDocs getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDocs usuario) {
        this.usuario = usuario;
    }

    public UsuarioDocs getUsuarioEdita() {
        return usuarioEdita;
    }

    public void setUsuarioEdita(UsuarioDocs usuarioEdita) {
        this.usuarioEdita = usuarioEdita;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this._id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tesauro other = (Tesauro) obj;
        return Objects.equals(this._id, other._id);
    }
    
    

}
