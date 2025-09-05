/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.ebilling.models;

/**
 *
 * @author andysanchez
 */
public class Cabecera {

  
    private String fechaEmision;
    private String cedulaRuc;
    private String propietario;
    private String direccion;
    private String correo;
    private String telefono;
    private Boolean esPasaporte;

    public Cabecera() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

//    public String getEntidadTributaria() {
//        return entidadTributaria;
//    }
//
//    public void setEntidadTributaria(String entidadTributaria) {
//        this.entidadTributaria = entidadTributaria;
//    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
    }
    
    public Boolean getEsPasaporte() {
        return esPasaporte;
    }

    public void setEsPasaporte(Boolean esPasaporte) {
        this.esPasaporte = esPasaporte;
    }

    @Override
    public String toString() {
        return "Cabecera{" + "fechaEmision=" + fechaEmision + ", cedulaRuc=" + cedulaRuc + ", propietario=" + propietario + ", direccion=" + direccion + ", correo=" + correo + ", telefono=" + telefono + '}';
    }

    
    
}
