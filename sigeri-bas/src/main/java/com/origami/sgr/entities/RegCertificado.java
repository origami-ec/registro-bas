/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_certificado", schema = "app")
@NamedQueries({
    @NamedQuery(name = "RegCertificado.findAll", query = "SELECT r FROM RegCertificado r")})
public class RegCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tipo_certificado")
    private Long tipoCertificado;

    @Column(name = "tipo_documento", length = 3)
    private String tipoDocumento;

    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "num_certificado")
    private BigInteger numCertificado;

    @JoinColumn(name = "ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegFicha ficha;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "descripcion_bien")
    private String descripcionBien;
    @Column(name = "user_creador")
    private Long userCreador;
    @Column(name = "certificado_impreso")
    private Boolean certificadoImpreso = Boolean.FALSE;
    @Column(name = "con_bienes")
    private Boolean conBienes = Boolean.TRUE;
    @Column(name = "user_edicion")
    private Long userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "secuencia")
    private Integer secuencia = 0;
    @Column(name = "registrador")
    private Long registrador;
    @Column(name = "beneficiario")
    private String beneficiario;
    @Column(name = "linderos_registrales")
    private String linderosRegistrales;
    @Column(name = "parroquia")
    private String parroquia;
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Column(name = "clave_catastral_old")
    private String claveCatastralOld;
    @Column(name = "url_foto")
    private String urlFoto;
    @Column(name = "cod_verificacion", length = 255)
    private String codVerificacion;
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "print_online")
    private Integer printOnline = 0;
    @Column(name = "date_print_online")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePrintOnline;
    @Column(name = "estado_mensaje")
    private Integer estadoMensaje = 1;

    @JoinColumn(name = "tarea_tramite", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private RegpTareasTramite tareaTramite;

    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegEnteInterviniente propietario;

    @JoinColumn(name = "cat_parroquia", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatParroquia catParroquia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificado", fetch = FetchType.LAZY)
    private Collection<RegCertificadoMovimiento> regCertificadoMovimientoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificado", fetch = FetchType.LAZY)
    private Collection<RegCertificadoPropietario> RegCertificadoPropietarioCollection;
    @Column(name = "documento")
    private Long documento;
    @Column(name = "nombre_solicitante", length = 500)
    private String nombreSolicitante;
    @Column(name = "uso_documento", length = 500)
    private String usoDocumento;

    @Column(name = "editable")
    private Boolean editable = Boolean.FALSE;

    public RegCertificado() {
    }

    public RegCertificado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public BigInteger getNumCertificado() {
        return numCertificado;
    }

    public void setNumCertificado(BigInteger numCertificado) {
        this.numCertificado = numCertificado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescripcionBien() {
        return descripcionBien;
    }

    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    public Boolean getCertificadoImpreso() {
        return certificadoImpreso;
    }

    public void setCertificadoImpreso(Boolean certificadoImpreso) {
        this.certificadoImpreso = certificadoImpreso;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Long getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Long tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public Long getUserCreador() {
        return userCreador;
    }

    public void setUserCreador(Long userCreador) {
        this.userCreador = userCreador;
    }

    public Long getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(Long userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Long getRegistrador() {
        return registrador;
    }

    public void setRegistrador(Long registrador) {
        this.registrador = registrador;
    }

    public RegpTareasTramite getTareaTramite() {
        return tareaTramite;
    }

    public void setTareaTramite(RegpTareasTramite tareaTramite) {
        this.tareaTramite = tareaTramite;
    }

    public RegEnteInterviniente getPropietario() {
        return propietario;
    }

    public void setPropietario(RegEnteInterviniente propietario) {
        this.propietario = propietario;
    }

    public Boolean getConBienes() {
        return conBienes;
    }

    public void setConBienes(Boolean conBienes) {
        this.conBienes = conBienes;
    }

    public String getLinderosRegistrales() {
        return linderosRegistrales;
    }

    public void setLinderosRegistrales(String linderosRegistrales) {
        this.linderosRegistrales = linderosRegistrales;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getClaveCatastralOld() {
        return claveCatastralOld;
    }

    public void setClaveCatastralOld(String claveCatastralOld) {
        this.claveCatastralOld = claveCatastralOld;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Collection<RegCertificadoMovimiento> getRegCertificadoMovimientoCollection() {
        return regCertificadoMovimientoCollection;
    }

    public void setRegCertificadoMovimientoCollection(Collection<RegCertificadoMovimiento> regCertificadoMovimientoCollection) {
        this.regCertificadoMovimientoCollection = regCertificadoMovimientoCollection;
    }

    public Collection<RegCertificadoPropietario> getRegCertificadoPropietarioCollection() {
        return RegCertificadoPropietarioCollection;
    }

    public void setRegCertificadoPropietarioCollection(Collection<RegCertificadoPropietario> RegCertificadoPropietarioCollection) {
        this.RegCertificadoPropietarioCollection = RegCertificadoPropietarioCollection;
    }

    public String getCodVerificacion() {
        return codVerificacion;
    }

    public void setCodVerificacion(String codVerificacion) {
        this.codVerificacion = codVerificacion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getPrintOnline() {
        return printOnline;
    }

    public void setPrintOnline(Integer printOnline) {
        this.printOnline = printOnline;
    }

    public Date getDatePrintOnline() {
        return datePrintOnline;
    }

    public void setDatePrintOnline(Date datePrintOnline) {
        this.datePrintOnline = datePrintOnline;
    }

    public Integer getEstadoMensaje() {
        return estadoMensaje;
    }

    public void setEstadoMensaje(Integer estadoMensaje) {
        this.estadoMensaje = estadoMensaje;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public CatParroquia getCatParroquia() {
        return catParroquia;
    }

    public void setCatParroquia(CatParroquia catParroquia) {
        this.catParroquia = catParroquia;
    }

    public String getClaseCertificado() {
        switch (this.tipoDocumento) {
            case "C01": 
                return "CERTIFICADO DE GRAVAMEN CON FICHA";
            case "C02": 
                return "CERTIFICADO DE GRAVAMEN HISTORIADO CON FICHA";
            case "C03": 
                return "CERTIFICADO DE GRAVAMEN LINDERADO CON FICHA";
            case "C04": 
                return "CERTIFICADO DE VENTAS CON FICHA";
            case "C05": 
                return "CERTIFICADO DE BIENES";
            case "C06": 
                return "CERTIFICACION GENERAL";
            case "C07": 
                return "COPIA DE RAZON DE INSCRIPCION";
            default:
                return "";
        }
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(String usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegCertificado)) {
            return false;
        }
        RegCertificado other = (RegCertificado) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegCertificado[ id=" + id + " ]";
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
    
}
