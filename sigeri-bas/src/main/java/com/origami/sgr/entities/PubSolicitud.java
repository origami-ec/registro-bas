/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import com.origami.sgr.models.ActosRequisito;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "pub_solicitud", schema = "flow")
public class PubSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites tramite;
    @Column(name = "sol_tipo_persona")
    private String solTipoPersona;
    @Column(name = "sol_tipo_doc")
    private String solTipoDoc;
    @Column(name = "sol_apellidos")
    private String solApellidos;
    @Column(name = "sol_nombres")
    private String solNombres;
    @Column(name = "sol_cedula")
    private String solCedula;
    @Column(name = "sol_provincia")
    private String solProvincia;
    @Column(name = "sol_ciudad")
    private String solCiudad;
    @Column(name = "sol_parroquia")
    private String solParroquia;
    @Column(name = "sol_calles")
    private String solCalles;
    @Column(name = "sol_numero_casa")
    private String solNumeroCasa;
    @Column(name = "sol_celular")
    private String solCelular;
    @Column(name = "sol_convencional")
    private String solConvencional;
    @Column(name = "sol_correo")
    private String solCorreo;
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    //ACTO ID VENTANILLA
    @Column(name = "tipo_solicitud")
    private Integer tipoSolicitud;
    @Column(name = "motivo_solicitud")
    private Integer motivoSolicitud;
    @Column(name = "otro_motivo")
    private String otroMotivo;
    @Column(name = "numero_ficha")
    private Integer numeroFicha;
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Column(name = "tipo_inmueble")
    private String tipoInmueble;
    @Column(name = "prop_estado_civil")
    private String propEstadoCivil;
    @Column(name = "prop_tipo_persona")
    private String propTipoPersona;
    @Column(name = "prop_tipo_doc")
    private String propTipoDoc;
    @Column(name = "prop_cedula")
    private String propCedula;
    @Column(name = "prop_nombres")
    private String propNombres;
    @Column(name = "prop_conyugue_cedula")
    private String propConyugueCedula;
    @Column(name = "prop_conyugue_nombres")
    private String propConyugueNombres;
    @Column(name = "prop_conyugue_apellidos")
    private String propConyugueApellidos;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "ben_tipo_persona")
    private String benTipoPersona;
    @Column(name = "ben_tipo_doc")
    private String benTipoDoc;
    @Column(name = "ben_documento")
    private String benDocumento;
    @Column(name = "ben_nombres")
    private String benNombres;
    @Column(name = "ben_apellidos")
    private String benApellidos;
    @Column(name = "ben_direccion")
    private String benDireccion;
    @Column(name = "ben_telefono")
    private String benTelefono;
    @Column(name = "ben_correo")
    private String benCorreo;
    @Column(name = "tipo_formulario")
    private String payframeUrl; //TIPO DE APLICACION WEB O VENTANILLA
    @Column(name = "prop_apellidos")
    private String propApellidos;
    @Column(name = "prop_apellido_materno")
    private String propApellidoMaterno;
    @Column(name = "lote") //LOTE DEPARTAMENTO
    private String lote;
    @Column(name = "num_inscripcion")
    private Integer numInscripcion;
    @Column(name = "num_repertorio")
    private Integer numRepertorio;
    @Column(name = "anio_inscripcion")
    private Integer anioInscripcion;
    @Column(name = "tipo_pago")
    private Boolean tipoPago;  //TRUE: VENTANILLA FALSE: PAGO EN LINEA
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "fecha_inscripcion1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_bien")
    private CtlgItem tipoBien;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_certificado")
    private CtlgItem tipoCertificado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_certificado_para")
    private CtlgItem tipoCertificadoPara;
    @Column(name = "copia_escritura_publica")
    private Boolean copiaEscrituraPublica;
    @Column(name = "doc_avaluo_catastro")
    private Boolean docAvaluoCatastro;
    @Column(name = "fecha_escritura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEscritura;
    @Column
    private Double total;
    @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PubSolicitudPropietarios> propietarios;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "estado")
    private String estado;
    @Column(name = "anio_ultima_transferencia")
    private Integer anioUltimaTransferencia;
    @Column(name = "anio_antecedente_solicitado")
    private Integer anioAntecedenteSolicitado;
    @Column(name = "sol_estado_civil")
    private String solEstadoCivil;
    @Column(name = "oid_document")
    private Long oidDocument;
    @Column(name = "oid_document2")
    private Long oidDocument2;
    @Column(name = "oid_document3")
    private Long oidDocument3;
    @Column(name = "id_solicitud_ventanilla")
    private Long idSolicitudVentanilla;
    @Column(name = "numero_tramite_inscripcion")
    private Long numeroTramiteInscripcion;
    @Column(name = "tiene_notificacion")
    private Boolean tieneNotificacion;
    @Column(name = "notificacion")
    private String notificacion;
    @Column(name = "link_pago")
    private String linkPago;
    @Column(name = "sector")
    private String sector;
    @Column(name = "manzana")
    private String manzana;
    @JoinColumn(name = "revisor", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser revisor;
    @Transient
    private Long numeroTramite;
    @Transient
    private List<ActosRequisito> requisitos;

    public PubSolicitud() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
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

    public String getPropApellidos() {
        return propApellidos;
    }

    public void setPropApellidos(String propApellidos) {
        this.propApellidos = propApellidos;
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

    public Integer getAnioInscripcion() {
        return anioInscripcion;
    }

    public void setAnioInscripcion(Integer anioInscripcion) {
        this.anioInscripcion = anioInscripcion;
    }

    public Boolean getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Boolean tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public CtlgItem getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(CtlgItem tipoBien) {
        this.tipoBien = tipoBien;
    }

    public CtlgItem getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(CtlgItem tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public CtlgItem getTipoCertificadoPara() {
        return tipoCertificadoPara;
    }

    public void setTipoCertificadoPara(CtlgItem tipoCertificadoPara) {
        this.tipoCertificadoPara = tipoCertificadoPara;
    }

    public Boolean getCopiaEscrituraPublica() {
        return copiaEscrituraPublica;
    }

    public void setCopiaEscrituraPublica(Boolean copiaEscrituraPublica) {
        this.copiaEscrituraPublica = copiaEscrituraPublica;
    }

    public Boolean getDocAvaluoCatastro() {
        return docAvaluoCatastro;
    }

    public void setDocAvaluoCatastro(Boolean docAvaluoCatastro) {
        this.docAvaluoCatastro = docAvaluoCatastro;
    }

    public Date getFechaEscritura() {
        return fechaEscritura;
    }

    public void setFechaEscritura(Date fechaEscritura) {
        this.fechaEscritura = fechaEscritura;
    }

    public List<PubSolicitudPropietarios> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<PubSolicitudPropietarios> propietarios) {
        this.propietarios = propietarios;
    }

    public String getPayframeUrl() {
        return payframeUrl;
    }

    public void setPayframeUrl(String payframeUrl) {
        this.payframeUrl = payframeUrl;
    }

    public String getPropApellidoMaterno() {
        return propApellidoMaterno;
    }

    public void setPropApellidoMaterno(String propApellidoMaterno) {
        this.propApellidoMaterno = propApellidoMaterno;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSolEstadoCivil() {
        return solEstadoCivil;
    }

    public void setSolEstadoCivil(String solEstadoCivil) {
        this.solEstadoCivil = solEstadoCivil;
    }

    public Long getOidDocument() {
        return oidDocument;
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

    public Long getNumeroTramiteInscripcion() {
        return numeroTramiteInscripcion;
    }

    public void setNumeroTramiteInscripcion(Long numeroTramiteInscripcion) {
        this.numeroTramiteInscripcion = numeroTramiteInscripcion;
    }

    public Boolean getTieneNotificacion() {
        return tieneNotificacion;
    }

    public void setTieneNotificacion(Boolean tieneNotificacion) {
        this.tieneNotificacion = tieneNotificacion;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getLinkPago() {
        return linkPago;
    }

    public void setLinkPago(String linkPago) {
        this.linkPago = linkPago;
    }

    public AclUser getRevisor() {
        return revisor;
    }

    public void setRevisor(AclUser revisor) {
        this.revisor = revisor;
    }

    public List<ActosRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<ActosRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    public Long getIdSolicitudVentanilla() {
        return idSolicitudVentanilla;
    }

    public void setIdSolicitudVentanilla(Long idSolicitudVentanilla) {
        this.idSolicitudVentanilla = idSolicitudVentanilla;
    }

    public Integer getAnioUltimaTransferencia() {
        return anioUltimaTransferencia;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public void setAnioUltimaTransferencia(Integer anioUltimaTransferencia) {
        this.anioUltimaTransferencia = anioUltimaTransferencia;
    }

    public Integer getAnioAntecedenteSolicitado() {
        return anioAntecedenteSolicitado;
    }

    public void setAnioAntecedenteSolicitado(Integer anioAntecedenteSolicitado) {
        this.anioAntecedenteSolicitado = anioAntecedenteSolicitado;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PubSolicitud{");
        sb.append("id=").append(id);
        sb.append(", tramite=").append(tramite);
        sb.append(", solTipoPersona=").append(solTipoPersona);
        sb.append(", solTipoDoc=").append(solTipoDoc);
        sb.append(", solApellidos=").append(solApellidos);
        sb.append(", solNombres=").append(solNombres);
        sb.append(", solCedula=").append(solCedula);
        sb.append(", solProvincia=").append(solProvincia);
        sb.append(", solCiudad=").append(solCiudad);
        sb.append(", solParroquia=").append(solParroquia);
        sb.append(", solCalles=").append(solCalles);
        sb.append(", solNumeroCasa=").append(solNumeroCasa);
        sb.append(", solCelular=").append(solCelular);
        sb.append(", solConvencional=").append(solConvencional);
        sb.append(", solCorreo=").append(solCorreo);
        sb.append(", fechaSolicitud=").append(fechaSolicitud);
        sb.append(", tipoSolicitud=").append(tipoSolicitud);
        sb.append(", motivoSolicitud=").append(motivoSolicitud);
        sb.append(", otroMotivo=").append(otroMotivo);
        sb.append(", numeroFicha=").append(numeroFicha);
        sb.append(", claveCatastral=").append(claveCatastral);
        sb.append(", tipoInmueble=").append(tipoInmueble);
        sb.append(", propEstadoCivil=").append(propEstadoCivil);
        sb.append(", propTipoPersona=").append(propTipoPersona);
        sb.append(", propTipoDoc=").append(propTipoDoc);
        sb.append(", propCedula=").append(propCedula);
        sb.append(", propNombres=").append(propNombres);
        sb.append(", propConyugueCedula=").append(propConyugueCedula);
        sb.append(", propConyugueNombres=").append(propConyugueNombres);
        sb.append(", propConyugueApellidos=").append(propConyugueApellidos);
        sb.append(", observacion=").append(observacion);
        sb.append(", benTipoPersona=").append(benTipoPersona);
        sb.append(", benTipoDoc=").append(benTipoDoc);
        sb.append(", benDocumento=").append(benDocumento);
        sb.append(", benNombres=").append(benNombres);
        sb.append(", benApellidos=").append(benApellidos);
        sb.append(", benDireccion=").append(benDireccion);
        sb.append(", benTelefono=").append(benTelefono);
        sb.append(", benCorreo=").append(benCorreo);
        sb.append(", payframeUrl=").append(payframeUrl);
        sb.append(", propApellidos=").append(propApellidos);
        sb.append(", propApellidoMaterno=").append(propApellidoMaterno);
        sb.append(", lote=").append(lote);
        sb.append(", numInscripcion=").append(numInscripcion);
        sb.append(", anioInscripcion=").append(anioInscripcion);
        sb.append(", tipoPago=").append(tipoPago);
        sb.append(", fechaInscripcion=").append(fechaInscripcion);
        sb.append(", tipoBien=").append(tipoBien);
        sb.append(", tipoCertificado=").append(tipoCertificado);
        sb.append(", tipoCertificadoPara=").append(tipoCertificadoPara);
        sb.append(", copiaEscrituraPublica=").append(copiaEscrituraPublica);
        sb.append(", docAvaluoCatastro=").append(docAvaluoCatastro);
        sb.append(", fechaEscritura=").append(fechaEscritura);
        sb.append(", total=").append(total);
        sb.append(", propietarios=").append(propietarios);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", estado=").append(estado);
        sb.append(", anioUltimaTransferencia=").append(anioUltimaTransferencia);
        sb.append(", anioAntecedenteSolicitado=").append(anioAntecedenteSolicitado);
        sb.append(", solEstadoCivil=").append(solEstadoCivil);
        sb.append(", oidDocument=").append(oidDocument);
        sb.append(", oidDocument2=").append(oidDocument2);
        sb.append(", oidDocument3=").append(oidDocument3);
        sb.append(", idSolicitudVentanilla=").append(idSolicitudVentanilla);
        sb.append(", numeroTramiteInscripcion=").append(numeroTramiteInscripcion);
        sb.append(", tieneNotificacion=").append(tieneNotificacion);
        sb.append(", notificacion=").append(notificacion);
        sb.append(", linkPago=").append(linkPago);
        sb.append(", revisor=").append(revisor);
        sb.append(", numeroTramite=").append(numeroTramite);
        sb.append(", requisitos=").append(requisitos);
        sb.append('}');
        return sb.toString();
    }

}
