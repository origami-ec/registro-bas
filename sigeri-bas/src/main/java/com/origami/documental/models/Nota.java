/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class Nota implements Serializable {
    
    private String titulo;
    private String nota;
    private String tipo;
    private String fecha;
    private UsuarioDocs usuario;
    private Integer ancho;
    private Integer alto;
    private Integer posicionX1;
    private Integer posicionX2;
    private Integer posicionY1;
    private Integer posicionY2;
    private String color;

    public Nota() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public UsuarioDocs getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDocs usuario) {
        this.usuario = usuario;
    }

    public Integer getAncho() {
        return ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getAlto() {
        return alto;
    }

    public void setAlto(Integer alto) {
        this.alto = alto;
    }

    public Integer getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(Integer posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public Integer getPosicionX2() {
        return posicionX2;
    }

    public void setPosicionX2(Integer posicionX2) {
        this.posicionX2 = posicionX2;
    }

    public Integer getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(Integer posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public Integer getPosicionY2() {
        return posicionY2;
    }

    public void setPosicionY2(Integer posicionY2) {
        this.posicionY2 = posicionY2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nota{");
        sb.append("titulo=").append(titulo);
        sb.append(", nota=").append(nota);
        sb.append(", tipo=").append(tipo);
        sb.append(", fecha=").append(fecha);
        sb.append(", usuario=").append(usuario);
        sb.append(", ancho=").append(ancho);
        sb.append(", alto=").append(alto);
        sb.append(", posicionX1=").append(posicionX1);
        sb.append(", posicionX2=").append(posicionX2);
        sb.append(", posicionY1=").append(posicionY1);
        sb.append(", posicionY2=").append(posicionY2);
        sb.append(", color=").append(color);
        sb.append('}');
        return sb.toString();
    }
    
}
