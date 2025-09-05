/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "indice_prop", schema = "historico")
public class IndiceProp implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ind_id")
    private Long indId;
    @Column(name = "ind_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indFecha;
    @Column(name = "ind_parroquia")
    private String indParroquia;
    @Column(name = "ind_contrato")
    private String indContrato;
    @Column(name = "ind_repertorio")
    private Integer indRepertorio;
    @Column(name = "ind_libro")
    private Integer indLibro;
    @Column(name = "ind_inscripcion")
    private Integer indInscripcion;
    @Column(name = "ind_fojas")
    private Integer indFojas;
    @Column(name = "ind_vigente")
    private Integer indVigente;
    @Column(name = "ind_fcancela")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indFcancela;
    @Column(name = "ind_contratante")
    private String indContratante;
    @Column(name = "ind_cedula")
    private String indCedula;
    @Column(name = "ind_nombre")
    private String indNombre;
    @Column(name = "ind_codigo")
    private String indCodigo;
    @Column(name = "ind_observa")
    private String indObserva;
    @Column(name = "ind_fechacel")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indFechacel;
    @Column(name = "ind_ventotal")
    private Integer indVentatotal;
    @Column(name = "ind_mi")
    private Integer indMi;
    @Column(name = "ind_nlinea")
    private Integer indNlinea;
    @Column(name = "ind_numero")
    private Integer indNumero;
    @Column(name = "ind_numjuicio")
    private String indNumJuicio;
    @Column(name = "ind_anio")
    private Integer indAnio;
    @Column(name = "ind_descripcion")
    private String indDescripcion;
    /*@Column(name = "ind_notaria")
    private String indNotaria;
    @Column(name = "ind_fmi")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indFmi;*/

    public IndiceProp() {
    }

    public Long getIndId() {
        return indId;
    }

    public void setIndId(Long indId) {
        this.indId = indId;
    }

    public Date getIndFecha() {
        return indFecha;
    }

    public void setIndFecha(Date indFecha) {
        this.indFecha = indFecha;
    }

    public String getIndParroquia() {
        return indParroquia;
    }

    public void setIndParroquia(String indParroquia) {
        this.indParroquia = indParroquia;
    }

    public String getIndContrato() {
        return indContrato;
    }

    public void setIndContrato(String indContrato) {
        this.indContrato = indContrato;
    }

    public Integer getIndRepertorio() {
        return indRepertorio;
    }

    public void setIndRepertorio(Integer indRepertorio) {
        this.indRepertorio = indRepertorio;
    }

    public Integer getIndLibro() {
        return indLibro;
    }

    public void setIndLibro(Integer indLibro) {
        this.indLibro = indLibro;
    }

    public Integer getIndInscripcion() {
        return indInscripcion;
    }

    public void setIndInscripcion(Integer indInscripcion) {
        this.indInscripcion = indInscripcion;
    }

    public Integer getIndFojas() {
        return indFojas;
    }

    public void setIndFojas(Integer indFojas) {
        this.indFojas = indFojas;
    }

    public Integer getIndVigente() {
        return indVigente;
    }

    public void setIndVigente(Integer indVigente) {
        this.indVigente = indVigente;
    }

    public Date getIndFcancela() {
        return indFcancela;
    }

    public void setIndFcancela(Date indFcancela) {
        this.indFcancela = indFcancela;
    }

    public String getIndContratante() {
        return indContratante;
    }

    public void setIndContratante(String indContratante) {
        this.indContratante = indContratante;
    }

    public String getIndCedula() {
        return indCedula;
    }

    public void setIndCedula(String indCedula) {
        this.indCedula = indCedula;
    }

    public String getIndNombre() {
        return indNombre;
    }

    public void setIndNombre(String indNombre) {
        this.indNombre = indNombre;
    }

    public String getIndCodigo() {
        return indCodigo;
    }

    public void setIndCodigo(String indCodigo) {
        this.indCodigo = indCodigo;
    }

    public String getIndObserva() {
        return indObserva;
    }

    public void setIndObserva(String indObserva) {
        this.indObserva = indObserva;
    }

    public Date getIndFechacel() {
        return indFechacel;
    }

    public void setIndFechacel(Date indFechacel) {
        this.indFechacel = indFechacel;
    }

    public Integer getIndVentatotal() {
        return indVentatotal;
    }

    public void setIndVentatotal(Integer indVentatotal) {
        this.indVentatotal = indVentatotal;
    }

    public Integer getIndMi() {
        return indMi;
    }

    public void setIndMi(Integer indMi) {
        this.indMi = indMi;
    }

    public Integer getIndNlinea() {
        return indNlinea;
    }

    public void setIndNlinea(Integer indNlinea) {
        this.indNlinea = indNlinea;
    }

    public Integer getIndNumero() {
        return indNumero;
    }

    public void setIndNumero(Integer indNumero) {
        this.indNumero = indNumero;
    }

    public String getIndNumJuicio() {
        return indNumJuicio;
    }

    public void setIndNumJuicio(String indNumJuicio) {
        this.indNumJuicio = indNumJuicio;
    }

    public Integer getIndAnio() {
        return indAnio;
    }

    public void setIndAnio(Integer indAnio) {
        this.indAnio = indAnio;
    }

    public String getIndDescripcion() {
        return indDescripcion;
    }

    public void setIndDescripcion(String indDescripcion) {
        this.indDescripcion = indDescripcion;
    }

    /*public String getIndNotaria() {
        return indNotaria;
    }

    public void setIndNotaria(String indNotaria) {
        this.indNotaria = indNotaria;
    }

    public Date getIndFmi() {
        return indFmi;
    }

    public void setIndFmi(Date indFmi) {
        this.indFmi = indFmi;
    }*/

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.indId);
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
        final IndiceProp other = (IndiceProp) obj;
        return Objects.equals(this.indId, other.indId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndiceProp{indId=").append(indId);
        sb.append(", indFecha=").append(indFecha);
        sb.append(", indParroquia=").append(indParroquia);
        sb.append(", indContrato=").append(indContrato);
        sb.append(", indRepertorio=").append(indRepertorio);
        sb.append(", indLibro=").append(indLibro);
        sb.append(", indInscripcion=").append(indInscripcion);
        sb.append(", indFojas=").append(indFojas);
        sb.append(", indVigente=").append(indVigente);
        sb.append(", indFcancela=").append(indFcancela);
        sb.append(", indContratante=").append(indContratante);
        sb.append(", indCedula=").append(indCedula);
        sb.append(", indNombre=").append(indNombre);
        sb.append(", indCodigo=").append(indCodigo);
        sb.append(", indObserva=").append(indObserva);
        sb.append(", indFechacel=").append(indFechacel);
        sb.append(", indVentatotal=").append(indVentatotal);
        sb.append(", indMi=").append(indMi);
        sb.append(", indNlinea=").append(indNlinea);
        sb.append(", indNumero=").append(indNumero);
        sb.append(", indNumJuicio=").append(indNumJuicio);
        sb.append(", indAnio=").append(indAnio);
        sb.append(", indDescripcion=").append(indDescripcion);
        sb.append('}');
        return sb.toString();
    }

}
