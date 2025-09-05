/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr;

/**
 *
 * @author EDWIN
 */
public class DatosUsuarioFirma {

    private String cedula;
    private String nombre;
    private String apellido;
    private String institucion = "";
    private String cargo = "";
    private String serial;
    private String fechaFirmaArchivo;
    private String entidadCertificadora;
    private Boolean selladoTiempo;
    private boolean certificadoDigitalValido;

    public DatosUsuarioFirma() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFechaFirmaArchivo() {
        return fechaFirmaArchivo;
    }

    public void setFechaFirmaArchivo(String fechaFirmaArchivo) {
        this.fechaFirmaArchivo = fechaFirmaArchivo;
    }

    public String getEntidadCertificadora() {
        return entidadCertificadora;
    }

    public void setEntidadCertificadora(String entidadCertificadora) {
        this.entidadCertificadora = entidadCertificadora;
    }

    public Boolean getSelladoTiempo() {
        return selladoTiempo;
    }

    public void setSelladoTiempo(Boolean selladoTiempo) {
        this.selladoTiempo = selladoTiempo;
    }

    public boolean isCertificadoDigitalValido() {
        return certificadoDigitalValido;
    }

    public void setCertificadoDigitalValido(boolean certificadoDigitalValido) {
        this.certificadoDigitalValido = certificadoDigitalValido;
    }

    @Override
    public String toString() {
        return "\tDatosUsuario\n"
                + "\t\t[cedula=" + cedula + "\n"
                + "\t\tnombre=" + nombre + "\n"
                + "\t\tapellido=" + apellido + "\n"
                + "\t\tinstitucion=" + institucion + "\n"
                + "\t\tcargo=" + cargo + "\n"
                + "\t\tentidadCertificadora=" + entidadCertificadora + "\n"
                + "\t\tselladoTiempo=" + selladoTiempo + "\n"
                + "\t\tcertificadoDigitalValido=" + certificadoDigitalValido + "\n"
                + "\t\t]";
    }
}
