/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * RESOLUCIÓN No. 039-NG-DINARDAP-2015
 *
 * @author Anyelo
 */
public class Formulario2 implements Serializable {

    private BigInteger idmovimiento;
    private String contrato;
    private String tipoacto;
    private String tipotramite;
    private String tipolibro;
    private Integer repertorio;             //OBLIGATORIO
    private Date fecharepertorio;           //OBLIGATORIO
    private Integer inscripcion;            //OBLIGATORIO
    private Date fechainscripcion;          //OBLIGATORIO
    private String interviniente;
    private String documento;
    private String estadocivil;
    private String conyugue;
    private String docconyugue;
    private String calidad;
    private BigDecimal cuantia;
    private String uuid;                    //OBLIGATORIO
    private BigInteger libro;
    private Date fechamodificacion;
    private String entidad;
    private String domicilio;
    private Date fechacelebracion;
    private String estado;
    private String observacion;
    private Integer folioinicio;
    private Integer foliofin;

    public Formulario2() {
    }

    public String getTipoacto() {
        return tipoacto;
    }

    public void setTipoacto(String tipoacto) {
        this.tipoacto = tipoacto;
    }

    public String getTipolibro() {
        return tipolibro;
    }

    public void setTipolibro(String tipolibro) {
        this.tipolibro = tipolibro;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Date getFecharepertorio() {
        return fecharepertorio;
    }

    public void setFecharepertorio(Date fecharepertorio) {
        this.fecharepertorio = fecharepertorio;
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

    public BigInteger getIdmovimiento() {
        return idmovimiento;
    }

    public void setIdmovimiento(BigInteger idmovimiento) {
        this.idmovimiento = idmovimiento;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getTipotramite() {
        return tipotramite;
    }

    public void setTipotramite(String tipotramite) {
        this.tipotramite = tipotramite;
    }

    public String getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(String interviniente) {
        this.interviniente = interviniente;
    }

    public String getDocumento() {
        if (documento == null) {
            documento = "**********";
        }
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getConyugue() {
        return conyugue;
    }

    public void setConyugue(String conyugue) {
        this.conyugue = conyugue;
    }

    public String getDocconyugue() {
        return docconyugue;
    }

    public void setDocconyugue(String docconyugue) {
        this.docconyugue = docconyugue;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public BigDecimal getCuantia() {
        if (cuantia == null) {
            cuantia = BigDecimal.ZERO;
        }
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigInteger getLibro() {
        return libro;
    }

    public void setLibro(BigInteger libro) {
        this.libro = libro;
    }

    public Date getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFechacelebracion() {
        return fechacelebracion;
    }

    public void setFechacelebracion(Date fechacelebracion) {
        this.fechacelebracion = fechacelebracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getFolioinicio() {
        return folioinicio;
    }

    public void setFolioinicio(Integer folioinicio) {
        this.folioinicio = folioinicio;
    }

    public Integer getFoliofin() {
        return foliofin;
    }

    public void setFoliofin(Integer foliofin) {
        this.foliofin = foliofin;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idmovimiento);
        hash = 29 * hash + Objects.hashCode(this.contrato);
        hash = 29 * hash + Objects.hashCode(this.tipoacto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Formulario2 other = (Formulario2) obj;
        if (!Objects.equals(this.contrato, other.contrato)) {
            return false;
        }
        if (!Objects.equals(this.tipoacto, other.tipoacto)) {
            return false;
        }
        if (!Objects.equals(this.idmovimiento, other.idmovimiento)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Formulario2{" + "idmovimiento=" + idmovimiento + ", contrato=" + contrato + ", tipoacto=" + tipoacto + ", tipotramite=" + tipotramite + ", tipolibro=" + tipolibro + ", repertorio=" + repertorio + ", inscripcion=" + inscripcion + '}';
    }

}
