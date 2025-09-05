/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Anyelo
 */
public class ReporteDatosUsuario implements Serializable {

    public static final long serialVersionUID = 1L;

    private Integer tipo;           //TIPO DE DATO: 1 - FICHA / 2 - ANTECEDENTE / 3 - INSCRIPCION / 4 - CERTIFICADO
    //DATOS DE FICHA REGISTRAL
    private Long ficha;             //NUMERO DE FICHA
    private String parroquia;       //NOMBRE PARROQUIA
    private String predio;          //TIPO DE PREDIO: URBANO - RURAL
    private String codigo;          //CODIGO PREDIAL
    private String sector;          //SECTOR DE LA UBICACION DEL BIEN
    //DATOS DE INSCRIPCION INGRESADA INGRESADO
    private Long tramite;           //NUMERO DE TRAMITE
    private String libro;           //NOMBRE DE LIBRO REGISTRAL
    private String acto;            //NOMBRE DE CONTRATO - ACTO
    private Integer repertorio;     //NUMERO DE REPERTORIO
    private Integer inscripcion;    //NUMERO DE INSCRIPCION
    private Date fechainscripcion;  //FECHA DE INSCRIPCION
    //DATOS DE CERTIFICADO REALIZADO
    private Long tipocertificado;   //TIPO DE CERTIFIFICADO
    private BigInteger certificado;       //NUMERO DE CERTIFICADO
    private Date emision;           //FECHA DE EMISION DE CERTIFICADO

    public ReporteDatosUsuario(Integer tipo, RegFicha ficha) {
        this.tipo = tipo;
        this.ficha = ficha.getNumFicha();
        if (ficha.getParroquia() != null) {
            this.parroquia = ficha.getParroquia().getDescripcion();
        }
        if (ficha.getTipoPredio() != null) {
            this.predio = ficha.getTipoPredio();
        }
        if (ficha.getClaveCatastral() != null) {
            this.codigo = ficha.getClaveCatastral();
        }
        if (ficha.getSector() != null) {
            this.sector = ficha.getSector();
        }
    }

    public ReporteDatosUsuario(Integer tipo, RegMovimiento mov) {
        this.tipo = tipo;
        if (mov.getTramite() != null) {
            this.tramite = mov.getTramite().getTramite().getNumTramite();
        }
        if (mov.getLibro() != null) {
            this.libro = mov.getLibro().getNombre();
        }
        if (mov.getActo() != null) {
            this.acto = mov.getActo().getNombre();
        }
        this.inscripcion = mov.getNumInscripcion();
        this.repertorio = mov.getNumRepertorio();
        this.fechainscripcion = mov.getFechaInscripcion();
    }

    public ReporteDatosUsuario(Integer tipo, RegCertificado cer) {
        this.tipo = tipo;
        this.tipocertificado = cer.getTipoCertificado();
        this.certificado = cer.getNumCertificado();
        this.emision = cer.getFechaEmision();
        if (cer.getFicha() != null) {
            this.ficha = cer.getFicha().getNumFicha();
            if (cer.getFicha().getParroquia() != null) {
                this.parroquia = cer.getFicha().getParroquia().getDescripcion();
            }
            if (cer.getFicha().getTipoPredio() != null) {
                this.predio = cer.getFicha().getTipoPredio();
            }
            if (cer.getFicha().getClaveCatastral() != null) {
                this.codigo = cer.getFicha().getClaveCatastral();
            }
            if (cer.getFicha().getSector() != null) {
                this.sector = cer.getFicha().getSector();
            }
        }
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Long getFicha() {
        return ficha;
    }

    public void setFicha(Long ficha) {
        this.ficha = ficha;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechainscripcion() {
        return fechainscripcion;
    }

    public void setFechainscripcion(Date fechainscripcion) {
        this.fechainscripcion = fechainscripcion;
    }

    public Long getTipocertificado() {
        return tipocertificado;
    }

    public void setTipocertificado(Long tipocertificado) {
        this.tipocertificado = tipocertificado;
    }

    public BigInteger getCertificado() {
        return certificado;
    }

    public void setCertificado(BigInteger certificado) {
        this.certificado = certificado;
    }

    public Date getEmision() {
        return emision;
    }

    public void setEmision(Date emision) {
        this.emision = emision;
    }

}
