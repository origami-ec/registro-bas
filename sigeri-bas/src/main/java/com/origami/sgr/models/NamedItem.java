/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gutya
 */
public class NamedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String nombre;
    private String usuario;
    private BigInteger cantidad = BigInteger.ZERO;
    private String fecha;
    private String color;
    private List<BigInteger> cantidades;
    private String tiempoEstimado;
    private String tiempoPromedioReal;
    //VARIABLES PARA LOS LIBROS 
    private BigInteger eliminados;
    private BigInteger pendientes;
    private BigInteger finalizados;
    private BigInteger num_tramite;

    public NamedItem() {
    }

    public NamedItem(BigInteger id, String nombre, BigInteger cantidad, String fecha, String color) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.color = color;
    }

    public NamedItem(BigInteger id) {
        this.id = id;
    }

    public NamedItem(BigInteger id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
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
        NamedItem other = (NamedItem) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<BigInteger> getCantidades() {
        if (cantidades == null) {
            cantidades = new ArrayList<>();
        }
        return cantidades;
    }

    public void setCantidades(List<BigInteger> cantidades) {
        this.cantidades = cantidades;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getTiempoPromedioReal() {
        return tiempoPromedioReal;
    }

    public void setTiempoPromedioReal(String tiempoPromedioReal) {
        this.tiempoPromedioReal = tiempoPromedioReal;
    }

    public BigInteger getEliminados() {
        return eliminados;
    }

    public void setEliminados(BigInteger eliminados) {
        this.eliminados = eliminados;
    }

    public BigInteger getPendientes() {
        return pendientes;
    }

    public void setPendientes(BigInteger pendientes) {
        this.pendientes = pendientes;
    }

    public BigInteger getFinalizados() {
        return finalizados;
    }

    public void setFinalizados(BigInteger finalizados) {
        this.finalizados = finalizados;
    }

    @Override
    public String toString() {
        return "{" + "nombre: '" + nombre + "', color: '" + color + "', cantidades: " + toStringCantidades(cantidades) + '}';
    }

    private String toStringCantidades(List<BigInteger> cants) {
        String cant = "";
        for (BigInteger i : cants) {
            cant = cant + ", " + i.toString() + "";
        }
        return "[ " + cant.substring(1, cant.length()) + " ]";
    }

    public BigInteger getNum_tramite() {
        return num_tramite;
    }

    public void setNum_tramite(BigInteger num_tramite) {
        this.num_tramite = num_tramite;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
