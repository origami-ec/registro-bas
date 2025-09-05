/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author gutya
 */
public class PubPersona implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long id;
    private String ciRuc;
    private String nombres;
    private String apellidos;
    private Boolean esPersona;
    private String direccion;
    private String representanteLegal;
    private String estado;
    private String nombreComercial;
    private String razonSocial;
    private String tituloProf;
    private String telefono1;
    private String telefono2;
    private String correo1;
    private String correo2;
    private Boolean excepcional;
    private String tipoIdentificacion;
    private Long estadoCivil;
    private Long codCiu;
    private String tipoDocumento;
    private String estadoCivilText;
    private String condicionCiudadano;
    private String fechaDefuncion;
    private Long fechaNacimientoLong;
    private Long fechaExpedicionLong;
    private Long fechaExpiracionLong;
    private String conyuge;
    private String fechaInicioActividades;
    private String actividadEconomica;
    private String cargoRepresentante;
    private String identificacionRepresentante;

    public PubPersona() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public Boolean getExcepcional() {
        return excepcional;
    }

    public void setExcepcional(Boolean excepcional) {
        this.excepcional = excepcional;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(Long estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Long getCodCiu() {
        return codCiu;
    }

    public void setCodCiu(Long codCiu) {
        this.codCiu = codCiu;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getEstadoCivilText() {
        return estadoCivilText;
    }

    public void setEstadoCivilText(String estadoCivilText) {
        this.estadoCivilText = estadoCivilText;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public Long getFechaNacimientoLong() {
        return fechaNacimientoLong;
    }

    public void setFechaNacimientoLong(Long fechaNacimientoLong) {
        this.fechaNacimientoLong = fechaNacimientoLong;
    }

    public Long getFechaExpedicionLong() {
        return fechaExpedicionLong;
    }

    public void setFechaExpedicionLong(Long fechaExpedicionLong) {
        this.fechaExpedicionLong = fechaExpedicionLong;
    }

    public Long getFechaExpiracionLong() {
        return fechaExpiracionLong;
    }

    public void setFechaExpiracionLong(Long fechaExpiracionLong) {
        this.fechaExpiracionLong = fechaExpiracionLong;
    }

    public String getConyuge() {
        return conyuge;
    }

    public void setConyuge(String conyuge) {
        this.conyuge = conyuge;
    }

    public String getFechaInicioActividades() {
        return fechaInicioActividades;
    }

    public void setFechaInicioActividades(String fechaInicioActividades) {
        this.fechaInicioActividades = fechaInicioActividades;
    }

    public String getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public String getCargoRepresentante() {
        return cargoRepresentante;
    }

    public void setCargoRepresentante(String cargoRepresentante) {
        this.cargoRepresentante = cargoRepresentante;
    }

    public String getIdentificacionRepresentante() {
        return identificacionRepresentante;
    }

    public void setIdentificacionRepresentante(String identificacionRepresentante) {
        this.identificacionRepresentante = identificacionRepresentante;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PubPersona{");
        sb.append("id=").append(id);
        sb.append(", ciRuc=").append(ciRuc);
        sb.append(", nombres=").append(nombres);
        sb.append(", apellidos=").append(apellidos);
        sb.append(", esPersona=").append(esPersona);
        sb.append(", direccion=").append(direccion);
        sb.append(", representanteLegal=").append(representanteLegal);
        sb.append(", estado=").append(estado);
        sb.append(", nombreComercial=").append(nombreComercial);
        sb.append(", razonSocial=").append(razonSocial);
        sb.append(", tituloProf=").append(tituloProf);
        sb.append(", telefono1=").append(telefono1);
        sb.append(", telefono2=").append(telefono2);
        sb.append(", correo1=").append(correo1);
        sb.append(", correo2=").append(correo2);
        sb.append(", excepcional=").append(excepcional);
        sb.append(", tipoIdentificacion=").append(tipoIdentificacion);
        sb.append(", estadoCivil=").append(estadoCivil);
        sb.append(", codCiu=").append(codCiu);
        sb.append(", tipoDocumento=").append(tipoDocumento);
        sb.append(", estadoCivilText=").append(estadoCivilText);
        sb.append(", condicionCiudadano=").append(condicionCiudadano);
        sb.append(", fechaDefuncion=").append(fechaDefuncion);
        sb.append(", fechaNacimientoLong=").append(fechaNacimientoLong);
        sb.append(", fechaExpedicionLong=").append(fechaExpedicionLong);
        sb.append(", fechaExpiracionLong=").append(fechaExpiracionLong);
        sb.append(", conyuge=").append(conyuge);
        sb.append(", fechaInicioActividades=").append(fechaInicioActividades);
        sb.append(", actividadEconomica=").append(actividadEconomica);
        sb.append(", cargoRepresentante=").append(cargoRepresentante);
        sb.append(", identificacionRepresentante=").append(identificacionRepresentante);
        sb.append('}');
        return sb.toString();
    }

}
