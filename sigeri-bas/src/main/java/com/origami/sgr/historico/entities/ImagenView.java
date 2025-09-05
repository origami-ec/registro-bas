/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "imagen_view", schema = "historico")
@Immutable
public class ImagenView implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "img_id")
    @Id
    private Long imgId;
    @Column(name = "oid")
    private Long oid;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "img_mi")
    private String imgMi;

    public ImagenView() {
    }
    
    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImgMi() {
        return imgMi;
    }

    public void setImgMi(String imgMi) {
        this.imgMi = imgMi;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.imgId);
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
        final ImagenView other = (ImagenView) obj;
        if (!Objects.equals(this.imgId, other.imgId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImagenView{imgId=").append(imgId);
        sb.append(", oid=").append(oid);
        sb.append(", nombre=").append(nombre);
        sb.append(", imgMi=").append(imgMi);
        sb.append('}');
        return sb.toString();
    }

}
