/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.util.Utils;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Angel Navarro
 */
public class DatoPublicoRegistroMC12 {

    private String apellidos;
    private String nombres;
    private String ci;
    private String tipocompareciente;
    private String tipocontrato;
    private String razonsocial;
    private Integer numinscripcion;
    private Date fechainsripcion;
    private String nombrerepresentante;
    private Date fechacancelacion;
    private String chasis;
    private String datosbien;
    private String registrador;
    private Date ultimamodificacion;
    private String codigounico = "";
    private String entidapublica;
    private String cantonnombre;
    private Date fechaescritura;
    private String estado;

    private String tipobien = "";
    private String motor = "";
    private String modelo = "";
    private String marca = "";
    private String anio = "";
    private String placa = "";

    public void procesarDatosBien() {
        if (Objects.nonNull(datosbien)) {
            String[] split = datosbien.split("\n");
            for (String dato : split) {
                if (dato.toUpperCase().startsWith("CLASE")) {
                    tipobien = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    tipobien = this.removeLastString(tipobien);
                    tipobien = tipobien.toUpperCase();
                } else if (dato.toUpperCase().startsWith("MOTOR")) {
                    motor = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    motor = this.removeLastString(motor);
                } else if (dato.toUpperCase().startsWith("MODELO")) {
                    modelo = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    modelo = this.removeLastString(modelo);
                } else if (dato.toUpperCase().startsWith("MARCA")) {
                    marca = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    marca = this.removeLastString(marca);
                } else if (dato.toUpperCase().startsWith("Año".toUpperCase())) {
                    anio = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    anio = this.removeLastString(anio);
                } else if (dato.toUpperCase().startsWith("placa".toUpperCase())) {
                    placa = dato.substring(dato.lastIndexOf(":") + 1).trim();
                    placa = this.removeLastString(placa);
                }
            }
        }
    }

    private String removeLastString(String text) {
        if (text == null) {
            return "";
        }
        if (text.endsWith(".")) {
            String aux = text.substring(0, text.lastIndexOf("."));
            return Utils.quitarTildes(aux);
        }
        return text;
    }

    public String getApellidos() {
        apellidos = Utils.quitarTildes(apellidos);
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        nombres = Utils.quitarTildes(nombres);
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getTipocompareciente() {
        return tipocompareciente;
    }

    public void setTipocompareciente(String tipocompareciente) {
        this.tipocompareciente = tipocompareciente;
    }

    public String getTipocontrato() {
        return tipocontrato;
    }

    public void setTipocontrato(String tipocontrato) {
        this.tipocontrato = tipocontrato;
    }

    public String getRazonsocial() {
        razonsocial = Utils.quitarTildes(razonsocial);
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public Integer getNuminscripcion() {
        return numinscripcion;
    }

    public void setNuminscripcion(Integer numinscripcion) {
        this.numinscripcion = numinscripcion;
    }

    public Date getFechainsripcion() {
        return fechainsripcion;
    }

    public void setFechainsripcion(Date fechainsripcion) {
        this.fechainsripcion = fechainsripcion;
    }

    public String getNombrerepresentante() {
        nombrerepresentante = Utils.quitarTildes(nombrerepresentante);
        return nombrerepresentante;
    }

    public void setNombrerepresentante(String nombrerepresentante) {
        this.nombrerepresentante = nombrerepresentante;
    }

    public Date getFechacancelacion() {
        return fechacancelacion;
    }

    public void setFechacancelacion(Date fechacancelacion) {
        this.fechacancelacion = fechacancelacion;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getDatosbien() {
        return datosbien;
    }

    public void setDatosbien(String datosbien) {
        this.datosbien = datosbien;
    }

    public String getRegistrador() {
        return registrador;
    }

    public void setRegistrador(String registrador) {
        this.registrador = registrador;
    }

    public Date getUltimamodificacion() {
        return ultimamodificacion;
    }

    public void setUltimamodificacion(Date ultimamodificacion) {
        this.ultimamodificacion = ultimamodificacion;
    }

    public String getCodigounico() {
        return codigounico;
    }

    public void setCodigounico(String codigounico) {
        this.codigounico = codigounico;
    }

    public String getEntidapublica() {
        entidapublica = Utils.quitarTildes(entidapublica);
        return entidapublica;
    }

    public void setEntidapublica(String entidapublica) {
        this.entidapublica = entidapublica;
    }

    public String getCantonnombre() {
        return cantonnombre;
    }

    public void setCantonnombre(String cantonnombre) {
        this.cantonnombre = cantonnombre;
    }

    public Date getFechaescritura() {
        return fechaescritura;
    }

    public void setFechaescritura(Date fechaescritura) {
        this.fechaescritura = fechaescritura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipobien() {
        return tipobien;
    }

    public void setTipobien(String tipobien) {
        this.tipobien = tipobien;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

}
