/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.mail.models;

import com.origami.mail.entities.CorreoUsuarios;
import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class ConfiguracionDto implements Serializable {

    private String servidor;
    private Integer puerto;
    private String usuario;
    private String clave;
    private String ssl;
    private String razonSocial;

    public ConfiguracionDto() {
    }
    
    public ConfiguracionDto(CorreoUsuarios temp) {
        this.servidor = temp.getServidor();
        this.puerto = temp.getPuerto();
        this.usuario = temp.getUsuario();
        this.clave = temp.getClave();
        this.ssl = temp.getSsl();
        this.razonSocial = temp.getRazonSocial();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConfiguracionDto{");
        sb.append("servidor=").append(servidor);
        sb.append(", puerto=").append(puerto);
        sb.append(", usuario=").append(usuario);
        sb.append(", clave=").append(clave);
        sb.append(", ssl=").append(ssl);
        sb.append(", razonSocial=").append(razonSocial);
        sb.append('}');
        return sb.toString();
    }
    
}
