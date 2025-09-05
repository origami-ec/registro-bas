/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SolicitudVP implements Serializable {

    private static final long serialVersionUID = 1L;

    /* DATOS DEL SOLICITANTE */
    private String sol_tipopersona;
    private String sol_tipodoc;
    private String sol_apellidos;
    private String sol_nombres;
    private String sol_cedula;
    private String sol_provincia;
    private String sol_ciudad;
    private String sol_parroquia;
    private String sol_calles;
    private String sol_numero_casa;
    private String sol_celular;
    private String sol_convencional;
    private String sol_correo;

    /* DATOS DE LA SOLICITUD */
    private Long fechaformulario;
    private Long fechainscribir;
    private Date fechasolicitud;
    private Date fechainscripcion;
    private Integer tiposolicitud; //1.HISTORIA DOMINIO - 2.BIEN RAIZ - 3.GENERAL
    private Integer motivosolicitud;
    private String otromotivo;
    private Integer numeroficha;
    private String clavecatastral;
    private Integer tipoinmueble;
    private String prop_estadocivil;
    private String prop_tipopersona;
    private String prop_tipodoc;
    private String prop_cedula;
    private String prop_nombres;
    private String prop_apellidos;
    private String prop_conyugue_cedula;
    private String prop_conyugue_nombres;
    private String prop_conyugue_apellidos;
    private String observacion;

    /* DATOS DEL BENEFICIARIO */
    private String ben_tipopersona;
    private String ben_tipodoc;
    private String ben_documento;
    private String ben_nombres;
    private String ben_apellidos;
    private String ben_direccion;
    private String ben_telefono;
    private String ben_correo;

    private Long oiddocumento;
    private Long oid_document1;
    private Long oid_document2;
    private Long oid_document3;

    public SolicitudVP() {
    }

    public String getSol_apellidos() {
        return sol_apellidos;
    }

    public void setSol_apellidos(String sol_apellidos) {
        this.sol_apellidos = sol_apellidos;
    }

    public String getSol_nombres() {
        return sol_nombres;
    }

    public void setSol_nombres(String sol_nombres) {
        this.sol_nombres = sol_nombres;
    }

    public String getSol_cedula() {
        return sol_cedula;
    }

    public void setSol_cedula(String sol_cedula) {
        this.sol_cedula = sol_cedula;
    }

    public String getSol_provincia() {
        return sol_provincia;
    }

    public void setSol_provincia(String sol_provincia) {
        this.sol_provincia = sol_provincia;
    }

    public String getSol_ciudad() {
        return sol_ciudad;
    }

    public void setSol_ciudad(String sol_ciudad) {
        this.sol_ciudad = sol_ciudad;
    }

    public String getSol_parroquia() {
        return sol_parroquia;
    }

    public void setSol_parroquia(String sol_parroquia) {
        this.sol_parroquia = sol_parroquia;
    }

    public String getSol_calles() {
        return sol_calles;
    }

    public void setSol_calles(String sol_calles) {
        this.sol_calles = sol_calles;
    }

    public String getSol_numero_casa() {
        return sol_numero_casa;
    }

    public void setSol_numero_casa(String sol_numero_casa) {
        this.sol_numero_casa = sol_numero_casa;
    }

    public String getSol_celular() {
        return sol_celular;
    }

    public void setSol_celular(String sol_celular) {
        this.sol_celular = sol_celular;
    }

    public String getSol_convencional() {
        return sol_convencional;
    }

    public void setSol_convencional(String sol_convencional) {
        this.sol_convencional = sol_convencional;
    }

    public String getSol_correo() {
        return sol_correo;
    }

    public void setSol_correo(String sol_correo) {
        this.sol_correo = sol_correo;
    }

    public Long getFechaformulario() {
        return fechaformulario;
    }

    public void setFechaformulario(Long fechaformulario) {
        this.fechaformulario = fechaformulario;
    }

    public Long getFechainscribir() {
        return fechainscribir;
    }

    public void setFechainscribir(Long fechainscribir) {
        this.fechainscribir = fechainscribir;
    }

    public Date getFechasolicitud() {
        return fechasolicitud;
    }

    public void setFechasolicitud(Date fechasolicitud) {
        this.fechasolicitud = fechasolicitud;
    }

    public Date getFechainscripcion() {
        return fechainscripcion;
    }

    public void setFechainscripcion(Date fechainscripcion) {
        this.fechainscripcion = fechainscripcion;
    }

    public Integer getTiposolicitud() {
        return tiposolicitud;
    }

    public void setTiposolicitud(Integer tiposolicitud) {
        this.tiposolicitud = tiposolicitud;
    }

    public Integer getMotivosolicitud() {
        return motivosolicitud;
    }

    public void setMotivosolicitud(Integer motivosolicitud) {
        this.motivosolicitud = motivosolicitud;
    }

    public String getOtromotivo() {
        return otromotivo;
    }

    public void setOtromotivo(String otromotivo) {
        this.otromotivo = otromotivo;
    }

    public Integer getNumeroficha() {
        return numeroficha;
    }

    public void setNumeroficha(Integer numeroficha) {
        this.numeroficha = numeroficha;
    }

    public String getClavecatastral() {
        return clavecatastral;
    }

    public void setClavecatastral(String clavecatastral) {
        this.clavecatastral = clavecatastral;
    }

    public Integer getTipoinmueble() {
        return tipoinmueble;
    }

    public void setTipoinmueble(Integer tipoinmueble) {
        this.tipoinmueble = tipoinmueble;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getBen_documento() {
        return ben_documento;
    }

    public void setBen_documento(String ben_documento) {
        this.ben_documento = ben_documento;
    }

    public String getBen_nombres() {
        return ben_nombres;
    }

    public void setBen_nombres(String ben_nombres) {
        this.ben_nombres = ben_nombres;
    }

    public String getBen_apellidos() {
        return ben_apellidos;
    }

    public void setBen_apellidos(String ben_apellidos) {
        this.ben_apellidos = ben_apellidos;
    }

    public String getBen_direccion() {
        return ben_direccion;
    }

    public void setBen_direccion(String ben_direccion) {
        this.ben_direccion = ben_direccion;
    }

    public String getBen_telefono() {
        return ben_telefono;
    }

    public void setBen_telefono(String ben_telefono) {
        this.ben_telefono = ben_telefono;
    }

    public String getBen_correo() {
        return ben_correo;
    }

    public void setBen_correo(String ben_correo) {
        this.ben_correo = ben_correo;
    }

    public String getSol_tipopersona() {
        return sol_tipopersona;
    }

    public void setSol_tipopersona(String sol_tipopersona) {
        this.sol_tipopersona = sol_tipopersona;
    }

    public String getBen_tipopersona() {
        return ben_tipopersona;
    }

    public void setBen_tipopersona(String ben_tipopersona) {
        this.ben_tipopersona = ben_tipopersona;
    }

    public String getSol_tipodoc() {
        return sol_tipodoc;
    }

    public void setSol_tipodoc(String sol_tipodoc) {
        this.sol_tipodoc = sol_tipodoc;
    }

    public String getBen_tipodoc() {
        return ben_tipodoc;
    }

    public void setBen_tipodoc(String ben_tipodoc) {
        this.ben_tipodoc = ben_tipodoc;
    }

    public String getProp_estadocivil() {
        return prop_estadocivil;
    }

    public void setProp_estadocivil(String prop_estadocivil) {
        this.prop_estadocivil = prop_estadocivil;
    }

    public String getProp_tipopersona() {
        return prop_tipopersona;
    }

    public void setProp_tipopersona(String prop_tipopersona) {
        this.prop_tipopersona = prop_tipopersona;
    }

    public String getProp_tipodoc() {
        return prop_tipodoc;
    }

    public void setProp_tipodoc(String prop_tipodoc) {
        this.prop_tipodoc = prop_tipodoc;
    }

    public String getProp_cedula() {
        return prop_cedula;
    }

    public void setProp_cedula(String prop_cedula) {
        this.prop_cedula = prop_cedula;
    }

    public String getProp_nombres() {
        return prop_nombres;
    }

    public void setProp_nombres(String prop_nombres) {
        this.prop_nombres = prop_nombres;
    }

    public String getProp_apellidos() {
        return prop_apellidos;
    }

    public void setProp_apellidos(String prop_apellidos) {
        this.prop_apellidos = prop_apellidos;
    }

    public String getProp_conyugue_cedula() {
        return prop_conyugue_cedula;
    }

    public void setProp_conyugue_cedula(String prop_conyugue_cedula) {
        this.prop_conyugue_cedula = prop_conyugue_cedula;
    }

    public String getProp_conyugue_nombres() {
        return prop_conyugue_nombres;
    }

    public void setProp_conyugue_nombres(String prop_conyugue_nombres) {
        this.prop_conyugue_nombres = prop_conyugue_nombres;
    }

    public String getProp_conyugue_apellidos() {
        return prop_conyugue_apellidos;
    }

    public void setProp_conyugue_apellidos(String prop_conyugue_apellidos) {
        this.prop_conyugue_apellidos = prop_conyugue_apellidos;
    }

    public Long getOiddocumento() {
        return oiddocumento;
    }

    public void setOiddocumento(Long oiddocumento) {
        this.oiddocumento = oiddocumento;
    }

    public Long getOid_document1() {
        return oid_document1;
    }

    public void setOid_document1(Long oid_document1) {
        this.oid_document1 = oid_document1;
    }

    public Long getOid_document2() {
        return oid_document2;
    }

    public void setOid_document2(Long oid_document2) {
        this.oid_document2 = oid_document2;
    }

    public Long getOid_document3() {
        return oid_document3;
    }

    public void setOid_document3(Long oid_document3) {
        this.oid_document3 = oid_document3;
    }

}
