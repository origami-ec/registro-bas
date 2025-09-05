/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models.index;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class TbBlob implements Serializable {

    private Long idBLob;
    private Long idTransaccion;
    private String extension;
    private Short orden;
    private byte[] imagen;
    private byte[] anota;
    private String fechaInscripcion;
    private Integer imagenes;

    public TbBlob() {
    }

    public TbBlob(Long idBLob) {
        this.idBLob = idBLob;
    }

    public Long getIdBLob() {
        return idBLob;
    }

    public void setIdBLob(Long idBLob) {
        this.idBLob = idBLob;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public byte[] getAnota() {
        return anota;
    }

    public void setAnota(byte[] anota) {
        this.anota = anota;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getImagenes() {
        return imagenes;
    }

    public void setImagenes(Integer imagenes) {
        this.imagenes = imagenes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TbBlob) {
            TbBlob ob = (TbBlob) obj;

            return ob.getIdBLob().equals(this.getIdBLob());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.idBLob);
        return hash;
    }

//    @Override
//    public String toString() {
//        return "TbBlob{" + "idBLob=" + idBLob + ", idTransaccion=" + idTransaccion + ", extension=" + extension + ", orden=" + orden + '}';
//    }
    @Override
    public String toString() {
        return "TbBlob{" + "idBLob=" + idBLob + ", idTransaccion=" + idTransaccion + ", extension=" + extension + ", orden=" + orden + ", imagen=" + imagen + ", anota=" + anota + '}';
    }
}
