/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.util.Date;

/**
 *
 * @author gutya
 */
public class PubSolicitudVentanilla {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String solTipoPersona;
    private String solTipoDoc;
    private String solApellidos;
    private String solNombres;
    private String solCedula;
    private String solProvincia;
    private String solCiudad;
    private String solParroquia;
    private String solCalles;
    private String solNumeroCasa;
    private String solCelular;
    private String solConvencional;
    private String solCorreo;
    private String solEstadoCivil;
    private Date fechaSolicitud;
    private Integer tipoSolicitud;
    private Integer motivoSolicitud;
    private String otroMotivo;
    private Integer numeroFicha;
    private String claveCatastral;
    private String tipoInmueble;
    private String propEstadoCivil;
    private String propTipoPersona;
    private String propTipoDoc;
    private String propCedula;
    private String propNombres;
    private String propApellidos;
    private String propConyugueCedula;
    private String propConyugueNombres;
    private String propConyugueApellidos;
    private String observacion;
    private String benTipoPersona;
    private String benTipoDoc;
    private String benDocumento;
    private String benNombres;
    private String benApellidos;
    private String benDireccion;
    private String benTelefono;
    private String benCorreo;
    private Short installmentsType;
    private String payframeUrl;
    private Date fechaEntrega;
    private Date fechaIngreso;
    private Date fechaInscripcion;
    private Integer numeroTramite;
    private Boolean sendEmail;
    private Long oidDocument;
    private Long oidDocument2;
    private Long oidDocument3;
    private String image1;
    private String image2;
    private String image3;
    private Long oidDocument1;
    private String lote;
    private Integer numInscripcion;
    private Long fechaSolicitudLong;
    private Long fechaInscripcionLong;
    private Double total;
    private Integer anioInscripcion;
    private String estado;
    private Boolean tipoPago;  //TRUE: VENTANILLA FALSE: PAGO EN LINEA
    private Integer cantidad;

    public PubSolicitudVentanilla(String propApellidos, String propNombres, String propCedula,
            Integer tipoSolicitud, Integer motivoSolicitud, String otroMotivo, Integer numeroTramite,
            Date fechaIngreso,
            Date fechaEntrega) {
        this.propApellidos = propApellidos;
        this.propNombres = propNombres;
        this.propCedula = propCedula;
        this.tipoSolicitud = tipoSolicitud;
        this.motivoSolicitud = motivoSolicitud;
        this.otroMotivo = otroMotivo;
        this.numeroTramite = numeroTramite;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        //this.avance = this.calculo();
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getFechaSolicitudLong() {
        return fechaSolicitudLong;
    }

    public void setFechaSolicitudLong(Long fechaSolicitudLong) {
        this.fechaSolicitudLong = fechaSolicitudLong;
    }

    public Long getFechaInscripcionLong() {
        return fechaInscripcionLong;
    }

    public void setFechaInscripcionLong(Long fechaInscripcionLong) {
        this.fechaInscripcionLong = fechaInscripcionLong;
    }

    public PubSolicitudVentanilla() {
    }

    public PubSolicitudVentanilla(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolTipoPersona() {
        return solTipoPersona;
    }

    public void setSolTipoPersona(String solTipoPersona) {
        this.solTipoPersona = solTipoPersona;
    }

    public String getSolTipoDoc() {
        return solTipoDoc;
    }

    public void setSolTipoDoc(String solTipoDoc) {
        this.solTipoDoc = solTipoDoc;
    }

    public String getSolApellidos() {
        return solApellidos;
    }

    public void setSolApellidos(String solApellidos) {
        this.solApellidos = solApellidos;
    }

    public String getSolNombres() {
        return solNombres;
    }

    public void setSolNombres(String solNombres) {
        this.solNombres = solNombres;
    }

    public String getSolCedula() {
        return solCedula;
    }

    public void setSolCedula(String solCedula) {
        this.solCedula = solCedula;
    }

    public String getSolProvincia() {
        return solProvincia;
    }

    public void setSolProvincia(String solProvincia) {
        this.solProvincia = solProvincia;
    }

    public String getSolCiudad() {
        return solCiudad;
    }

    public void setSolCiudad(String solCiudad) {
        this.solCiudad = solCiudad;
    }

    public String getSolParroquia() {
        return solParroquia;
    }

    public void setSolParroquia(String solParroquia) {
        this.solParroquia = solParroquia;
    }

    public String getSolCalles() {
        return solCalles;
    }

    public void setSolCalles(String solCalles) {
        this.solCalles = solCalles;
    }

    public String getSolNumeroCasa() {
        return solNumeroCasa;
    }

    public void setSolNumeroCasa(String solNumeroCasa) {
        this.solNumeroCasa = solNumeroCasa;
    }

    public String getSolCelular() {
        return solCelular;
    }

    public void setSolCelular(String solCelular) {
        this.solCelular = solCelular;
    }

    public String getSolConvencional() {
        return solConvencional;
    }

    public void setSolConvencional(String solConvencional) {
        this.solConvencional = solConvencional;
    }

    public String getSolCorreo() {
        return solCorreo;
    }

    public void setSolCorreo(String solCorreo) {
        this.solCorreo = solCorreo;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(Integer tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public Integer getMotivoSolicitud() {
        return motivoSolicitud;
    }

    public void setMotivoSolicitud(Integer motivoSolicitud) {
        this.motivoSolicitud = motivoSolicitud;
    }

    public String getOtroMotivo() {
        return otroMotivo;
    }

    public void setOtroMotivo(String otroMotivo) {
        this.otroMotivo = otroMotivo;
    }

    public Integer getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Integer numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public String getPropEstadoCivil() {
        return propEstadoCivil;
    }

    public void setPropEstadoCivil(String propEstadoCivil) {
        this.propEstadoCivil = propEstadoCivil;
    }

    public String getPropTipoPersona() {
        return propTipoPersona;
    }

    public void setPropTipoPersona(String propTipoPersona) {
        this.propTipoPersona = propTipoPersona;
    }

    public String getPropTipoDoc() {
        return propTipoDoc;
    }

    public void setPropTipoDoc(String propTipoDoc) {
        this.propTipoDoc = propTipoDoc;
    }

    public String getPropCedula() {
        return propCedula;
    }

    public void setPropCedula(String propCedula) {
        this.propCedula = propCedula;
    }

    public String getPropNombres() {
        return propNombres;
    }

    public void setPropNombres(String propNombres) {
        this.propNombres = propNombres;
    }

    public String getPropApellidos() {
        return propApellidos;
    }

    public void setPropApellidos(String propApellidos) {
        this.propApellidos = propApellidos;
    }

    public String getPropConyugueCedula() {
        return propConyugueCedula;
    }

    public void setPropConyugueCedula(String propConyugueCedula) {
        this.propConyugueCedula = propConyugueCedula;
    }

    public String getPropConyugueNombres() {
        return propConyugueNombres;
    }

    public void setPropConyugueNombres(String propConyugueNombres) {
        this.propConyugueNombres = propConyugueNombres;
    }

    public String getPropConyugueApellidos() {
        return propConyugueApellidos;
    }

    public void setPropConyugueApellidos(String propConyugueApellidos) {
        this.propConyugueApellidos = propConyugueApellidos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getBenTipoPersona() {
        return benTipoPersona;
    }

    public void setBenTipoPersona(String benTipoPersona) {
        this.benTipoPersona = benTipoPersona;
    }

    public String getBenTipoDoc() {
        return benTipoDoc;
    }

    public void setBenTipoDoc(String benTipoDoc) {
        this.benTipoDoc = benTipoDoc;
    }

    public String getBenDocumento() {
        return benDocumento;
    }

    public void setBenDocumento(String benDocumento) {
        this.benDocumento = benDocumento;
    }

    public String getBenNombres() {
        return benNombres;
    }

    public void setBenNombres(String benNombres) {
        this.benNombres = benNombres;
    }

    public String getBenApellidos() {
        return benApellidos;
    }

    public void setBenApellidos(String benApellidos) {
        this.benApellidos = benApellidos;
    }

    public String getBenDireccion() {
        return benDireccion;
    }

    public void setBenDireccion(String benDireccion) {
        this.benDireccion = benDireccion;
    }

    public String getBenTelefono() {
        return benTelefono;
    }

    public void setBenTelefono(String benTelefono) {
        this.benTelefono = benTelefono;
    }

    public String getBenCorreo() {
        return benCorreo;
    }

    public void setBenCorreo(String benCorreo) {
        this.benCorreo = benCorreo;
    }

    public Short getInstallmentsType() {
        return installmentsType;
    }

    public void setInstallmentsType(Short installmentsType) {
        this.installmentsType = installmentsType;
    }

    public String getPayframeUrl() {
        return payframeUrl;
    }

    public void setPayframeUrl(String payframeUrl) {
        this.payframeUrl = payframeUrl;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Integer numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Long getOidDocument() {
        return oidDocument;
    }

    public Long getOidDocument1() {
        return oidDocument1;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public void setOidDocument(Long oidDocument) {
        this.oidDocument = oidDocument;
    }

    public Long getOidDocument2() {
        return oidDocument2;
    }

    public void setOidDocument2(Long oidDocument2) {
        this.oidDocument2 = oidDocument2;
    }

    public Long getOidDocument3() {
        return oidDocument3;
    }

    public void setOidDocument3(Long oidDocument3) {
        this.oidDocument3 = oidDocument3;
    }

    public void setOidDocument1(Long oidDocument1) {
        this.oidDocument1 = oidDocument1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getSolEstadoCivil() {
        return solEstadoCivil;
    }

    public void setSolEstadoCivil(String solEstadoCivil) {
        this.solEstadoCivil = solEstadoCivil;
    }

    public Integer getAnioInscripcion() {
        return anioInscripcion;
    }

    public void setAnioInscripcion(Integer anioInscripcion) {
        this.anioInscripcion = anioInscripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Boolean tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString() {
        return "PubSolicitud{"
                + "id=" + id
                + ", solTipoPersona='" + solTipoPersona + '\''
                + ", solTipoDoc='" + solTipoDoc + '\''
                + ", solApellidos='" + solApellidos + '\''
                + ", solNombres='" + solNombres + '\''
                + ", solCedula='" + solCedula + '\''
                + ", solProvincia='" + solProvincia + '\''
                + ", solCiudad='" + solCiudad + '\''
                + ", solParroquia='" + solParroquia + '\''
                + ", solCalles='" + solCalles + '\''
                + ", solNumeroCasa='" + solNumeroCasa + '\''
                + ", solCelular='" + solCelular + '\''
                + ", solConvencional='" + solConvencional + '\''
                + ", solCorreo='" + solCorreo + '\''
                + ", solEstadoCivil='" + solEstadoCivil + '\''
                + ", fechaSolicitud=" + fechaSolicitud
                + ", tipoSolicitud=" + tipoSolicitud
                + ", motivoSolicitud=" + motivoSolicitud
                + ", otroMotivo='" + otroMotivo + '\''
                + ", numeroFicha=" + numeroFicha
                + ", claveCatastral='" + claveCatastral + '\''
                + ", tipoInmueble='" + tipoInmueble + '\''
                + ", propEstadoCivil='" + propEstadoCivil + '\''
                + ", propTipoPersona='" + propTipoPersona + '\''
                + ", propTipoDoc='" + propTipoDoc + '\''
                + ", propCedula='" + propCedula + '\''
                + ", propNombres='" + propNombres + '\''
                + ", propApellidos='" + propApellidos + '\''
                + ", propConyugueCedula='" + propConyugueCedula + '\''
                + ", propConyugueNombres='" + propConyugueNombres + '\''
                + ", propConyugueApellidos='" + propConyugueApellidos + '\''
                + ", observacion='" + observacion + '\''
                + ", benTipoPersona='" + benTipoPersona + '\''
                + ", benTipoDoc='" + benTipoDoc + '\''
                + ", benDocumento='" + benDocumento + '\''
                + ", benNombres='" + benNombres + '\''
                + ", benApellidos='" + benApellidos + '\''
                + ", benDireccion='" + benDireccion + '\''
                + ", benTelefono='" + benTelefono + '\''
                + ", benCorreo='" + benCorreo + '\''
                + ", installmentsType=" + installmentsType
                + ", payframeUrl='" + payframeUrl + '\''
                + ", fechaEntrega=" + fechaEntrega
                + ", fechaIngreso=" + fechaIngreso
                + ", fechaInscripcion=" + fechaInscripcion
                + ", numeroTramite=" + numeroTramite
                + ", sendEmail=" + sendEmail
                + ", oidDocument=" + oidDocument
                + ", oidDocument2=" + oidDocument2
                + ", oidDocument3=" + oidDocument3
                + ", image1='" + image1 + '\''
                + ", image2='" + image2 + '\''
                + ", image3='" + image3 + '\''
                + ", oidDocument1=" + oidDocument1
                + ", lote='" + lote + '\''
                + ", numInscripcion=" + numInscripcion
                + ", fechaSolicitudLong=" + fechaSolicitudLong
                + ", fechaInscripcionLong=" + fechaInscripcionLong
                + ", total=" + total
                + ", anioInscripcion='" + anioInscripcion + '\''
                + '}';
    }
}

