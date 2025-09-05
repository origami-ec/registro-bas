/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "consulta_certificado", schema = "app")
public class ConsultaCertificadoView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "liquidacion")
    private Long liquidacion;
    @Column(name = "num_tramite_rp")
    private Long numTramiteRp;
    @Column(name = "propietarios")
    private String propietarios;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "contrato")
    private String contrato;
    @Column(name = "idcertificado")
    private Long idcertificado;
    @Column(name = "tipo_certificado")
    private Long tipoCertificado;
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "num_certificado")
    private BigInteger numCertificado;
    @Column(name = "ficha")
    private Long ficha;
    @Column(name = "num_ficha")
    private Long numFicha;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "descripcion_bien")
    private String descripcionBien;
    @Column(name = "clave_catastral")
    private String claveCatastral;

    public String getClaseCertificado() {
        if (tipoDocumento != null) {
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
        } else {
            return contrato;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(Long liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Long getNumTramiteRp() {
        return numTramiteRp;
    }

    public void setNumTramiteRp(Long numTramiteRp) {
        this.numTramiteRp = numTramiteRp;
    }

    public String getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(String propietarios) {
        this.propietarios = propietarios;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public Long getIdcertificado() {
        return idcertificado;
    }

    public void setIdcertificado(Long idcertificado) {
        this.idcertificado = idcertificado;
    }

    public Long getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Long tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public BigInteger getNumCertificado() {
        return numCertificado;
    }

    public void setNumCertificado(BigInteger numCertificado) {
        this.numCertificado = numCertificado;
    }

    public Long getFicha() {
        return ficha;
    }

    public void setFicha(Long ficha) {
        this.ficha = ficha;
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
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

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConsultaCertificadoView{id=").append(id);
        sb.append(", liquidacion=").append(liquidacion);
        sb.append(", numTramiteRp=").append(numTramiteRp);
        sb.append(", propietarios=").append(propietarios);
        sb.append(", fechaIngreso=").append(fechaIngreso);
        sb.append(", contrato=").append(contrato);
        sb.append(", idcertificado=").append(idcertificado);
        sb.append(", tipoCertificado=").append(tipoCertificado);
        sb.append(", tipoDocumento=").append(tipoDocumento);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", numCertificado=").append(numCertificado);
        sb.append(", ficha=").append(ficha);
        sb.append(", numFicha=").append(numFicha);
        sb.append(", fechaEmision=").append(fechaEmision);
        sb.append(", observacion=").append(observacion);
        sb.append(", descripcionBien=").append(descripcionBien);
        sb.append(", claveCatastral=").append(claveCatastral);
        sb.append('}');
        return sb.toString();
    }

}
