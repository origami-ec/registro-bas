/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.mail.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
 * @author eduar
 */
@Entity
@Table(name = "correo_cargas", schema = "mail")
public class CorreoCargas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "correo_usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CorreoUsuarios correoUsuario;
    @Column(name = "peso")
    private Integer peso;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public CorreoCargas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CorreoUsuarios getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(CorreoUsuarios correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final CorreoCargas other = (CorreoCargas) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorreoCargas{");
        sb.append("id=").append(id);
        sb.append(", correoUsuario=").append(correoUsuario);
        sb.append(", peso=").append(peso);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", fecha=").append(fecha);
        sb.append('}');
        return sb.toString();
    }
    
}
