/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "consulta_registral", schema = "app")
public class ConsultaRegistralView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "ced_ruc")
    private String cedRuc;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "idmovimiento")
    private Long idmovimiento;
    @Column(name = "idlibro")
    private Long idlibro;
    @Column(name = "libro")
    private String libro;
    @Column(name = "tipolibro")
    private Integer tipolibro;
    @Column(name = "contrato")
    private String contrato;
    @Column(name = "papel")
    private String papel;
    @Column(name = "registro")
    private Integer registro;
    @Column(name = "estado_movimiento")
    private String estadoMovimiento;
    @Column(name = "num_inscripcion")
    private Integer inscripcion;
    @Column(name = "num_repertorio")
    private Integer repertorio;
    @Column(name = "fecha_repertorio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRepertorio;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "periodo")
    private Integer periodo;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "numero_ficha")
    private String numeroFicha;
    @Column(name = "descripcion_ficha")
    private String descripcionFicha;
    @Column(name = "num_oficio")
    private String numOficio;
    @Column(name = "num_juicio")
    private String numJuicio;
    @Column(name = "inscriptor")
    private String inscriptor;

    public ConsultaRegistralView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdmovimiento() {
        return idmovimiento;
    }

    public void setIdmovimiento(Long idmovimiento) {
        this.idmovimiento = idmovimiento;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getEstadoMovimiento() {
        return estadoMovimiento;
    }

    public void setEstadoMovimiento(String estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Date getFechaRepertorio() {
        return fechaRepertorio;
    }

    public void setFechaRepertorio(Date fechaRepertorio) {
        this.fechaRepertorio = fechaRepertorio;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getIdlibro() {
        return idlibro;
    }

    public void setIdlibro(Long idlibro) {
        this.idlibro = idlibro;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Integer getTipolibro() {
        return tipolibro;
    }

    public void setTipolibro(Integer tipolibro) {
        this.tipolibro = tipolibro;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getDescripcionFicha() {
        return descripcionFicha;
    }

    public void setDescripcionFicha(String descripcionFicha) {
        this.descripcionFicha = descripcionFicha;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getNumOficio() {
        return numOficio;
    }

    public void setNumOficio(String numOficio) {
        this.numOficio = numOficio;
    }

    public String getNumJuicio() {
        return numJuicio;
    }

    public void setNumJuicio(String numJuicio) {
        this.numJuicio = numJuicio;
    }

    public String getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(String inscriptor) {
        this.inscriptor = inscriptor;
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
        if (!(object instanceof ConsultaRegistralView)) {
            return false;
        }
        ConsultaRegistralView other = (ConsultaRegistralView) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConsultaRegistralView{");
        sb.append("id=").append(id);
        sb.append(", cedRuc=").append(cedRuc);
        sb.append(", nombre=").append(nombre);
        sb.append(", idmovimiento=").append(idmovimiento);
        sb.append(", idlibro=").append(idlibro);
        sb.append(", libro=").append(libro);
        sb.append(", tipolibro=").append(tipolibro);
        sb.append(", contrato=").append(contrato);
        sb.append(", papel=").append(papel);
        sb.append(", registro=").append(registro);
        sb.append(", estadoMovimiento=").append(estadoMovimiento);
        sb.append(", inscripcion=").append(inscripcion);
        sb.append(", repertorio=").append(repertorio);
        sb.append(", fechaRepertorio=").append(fechaRepertorio);
        sb.append(", fechaInscripcion=").append(fechaInscripcion);
        sb.append(", periodo=").append(periodo);
        sb.append(", observacion=").append(observacion);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", numeroFicha=").append(numeroFicha);
        sb.append(", descripcionFicha=").append(descripcionFicha);
        sb.append(", numOficio=").append(numOficio);
        sb.append(", numJuicio=").append(numJuicio);
        sb.append(", inscriptor=").append(inscriptor);
        sb.append('}');
        return sb.toString();
    }

}
