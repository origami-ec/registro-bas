/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

/**
 *
 * @author ORIGAMI
 */
public class ImagenNota {

    private String archivoId;
    private String tipo;
    private Integer indiceImagen;
    private Integer indiceNota;
    private Nota nota;

    public ImagenNota() {
    }

    public ImagenNota(String archivoId, String tipo, Nota nota) {
        this.archivoId = archivoId;
        this.tipo = tipo;
        this.nota = nota;
    }

    public String getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(String archivoId) {
        this.archivoId = archivoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Integer getIndiceImagen() {
        return indiceImagen;
    }

    public void setIndiceImagen(Integer indiceImagen) {
        this.indiceImagen = indiceImagen;
    }

    public Integer getIndiceNota() {
        return indiceNota;
    }

    public void setIndiceNota(Integer indiceNota) {
        this.indiceNota = indiceNota;
    }

}
