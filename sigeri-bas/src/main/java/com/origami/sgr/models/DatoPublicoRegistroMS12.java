/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.util.Utils;
import java.util.Date;

/**
 *
 * @author Angel Navarro
 */
public class DatoPublicoRegistroMS12 {

    private String clienombre;
    private String ruc;
    private String tipocompania;
    private Date fechainsripcion;
    private String apellidos;
    private String nombres;
    private String numeroidentificacion;
    private String cargonombre;
    private String tipocompareciente;
    private String disposicion;
    private String autoridaddisposicion;
    private Date fechaescritura;
    private String notaria;
    private String cantonnombre;
    private String tipotramite;
    private String ubicaciondato;
    private String codigounico = "";
    private String estado;
    private Date fechadisposicion;
    private Date ultimamodificacion;
    private String numerodisposicion = "";

    private String removeLastString(String text) {
        if (text == null) {
            return "";
        }
        if (text.endsWith(".")) {
            return text.substring(0, text.lastIndexOf("."));
        }
        return text;
    }

    public String getClienombre() {
        this.clienombre = Utils.quitarTildes(clienombre);
        return clienombre;
    }

    public void setClienombre(String clienombre) {
        this.clienombre = clienombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTipocompania() {
        return tipocompania;
    }

    public void setTipocompania(String tipocompania) {
        this.tipocompania = tipocompania;
    }

    public Date getFechainsripcion() {
        return fechainsripcion;
    }

    public void setFechainsripcion(Date fechainsripcion) {
        this.fechainsripcion = fechainsripcion;
    }

    public String getApellidos() {
        this.apellidos = Utils.quitarTildes(apellidos);
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        this.nombres = Utils.quitarTildes(nombres);
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNumeroidentificacion() {
        return numeroidentificacion;
    }

    public void setNumeroidentificacion(String numeroidentificacion) {
        this.numeroidentificacion = numeroidentificacion;
    }

    public String getCargonombre() {
        return cargonombre;
    }

    public void setCargonombre(String cargonombre) {
        this.cargonombre = cargonombre;
    }

    public String getTipocompareciente() {
        return tipocompareciente;
    }

    public void setTipocompareciente(String tipocompareciente) {
        this.tipocompareciente = tipocompareciente;
    }

    public String getDisposicion() {
        this.disposicion = Utils.quitarTildes(disposicion);
        return disposicion;
    }

    public void setDisposicion(String disposicion) {
        this.disposicion = disposicion;
    }

    public String getAutoridaddisposicion() {
        return autoridaddisposicion;
    }

    public void setAutoridaddisposicion(String autoridaddisposicion) {
        this.autoridaddisposicion = autoridaddisposicion;
    }

    public Date getFechaescritura() {
        return fechaescritura;
    }

    public void setFechaescritura(Date fechaescritura) {
        this.fechaescritura = fechaescritura;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public String getCantonnombre() {
        return cantonnombre;
    }

    public void setCantonnombre(String cantonnombre) {
        this.cantonnombre = cantonnombre;
    }

    public String getTipotramite() {
        return tipotramite;
    }

    public void setTipotramite(String tipotramite) {
        this.tipotramite = tipotramite;
    }

    public String getUbicaciondato() {
        return ubicaciondato;
    }

    public void setUbicaciondato(String ubicaciondato) {
        this.ubicaciondato = ubicaciondato;
    }

    public String getCodigounico() {
        return codigounico;
    }

    public void setCodigounico(String codigounico) {
        this.codigounico = codigounico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechadisposicion() {
        return fechadisposicion;
    }

    public void setFechadisposicion(Date fechadisposicion) {
        this.fechadisposicion = fechadisposicion;
    }

    public String getNumerodisposicion() {
        return numerodisposicion;
    }

    public void setNumerodisposicion(String numerodisposicion) {
        this.numerodisposicion = numerodisposicion;
    }

    public Date getUltimamodificacion() {
        return ultimamodificacion;
    }

    public void setUltimamodificacion(Date ultimamodificacion) {
        this.ultimamodificacion = ultimamodificacion;
    }

    

}
