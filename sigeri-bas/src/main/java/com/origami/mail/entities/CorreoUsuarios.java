/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.mail.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

/**
 *
 * @author eduar
 */
@Entity
@Table(name = "correo_usuarios", schema = "mail")
public class CorreoUsuarios implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "servidor")
    private String servidor;
    @Column(name = "puerto")
    private Integer puerto;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "clave")
    private String clave;
    @Column(name = "ssl")
    private String ssl;
    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "estado")
    private Boolean estado;
    
    @Where(clause = "CAST(fecha AS DATE) = CAST(now() AS DATE)")
    @OneToMany(mappedBy = "correoUsuario", fetch = FetchType.LAZY)
    private Collection<CorreoCargas> correoCargasCollection;

    public CorreoUsuarios() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Collection<CorreoCargas> getCorreoCargasCollection() {
        return correoCargasCollection;
    }

    public void setCorreoCargasCollection(Collection<CorreoCargas> correoCargasCollection) {
        this.correoCargasCollection = correoCargasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final CorreoUsuarios other = (CorreoUsuarios) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorreoUsuarios{");
        sb.append("id=").append(id);
        sb.append(", servidor=").append(servidor);
        sb.append(", puerto=").append(puerto);
        sb.append(", usuario=").append(usuario);
        sb.append(", clave=").append(clave);
        sb.append(", ssl=").append(ssl);
        sb.append(", razonSocial=").append(razonSocial);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }
    
}
