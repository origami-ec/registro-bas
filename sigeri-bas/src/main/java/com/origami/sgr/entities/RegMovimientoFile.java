/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "reg_movimiento_file", schema = "app")
public class RegMovimientoFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "oid_file")
    private long oidFile;
    @Size(min = 1, max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 50)
    @Column(name = "content_type")
    private String contentType;
    @Size(max = 20)
    @Column(name = "idtras")
    private String idtras;
    @Size(max = 20)
    @Column(name = "anio")
    private String anio;
    @Size(max = 50)
    @Column(name = "carpeta")
    private String carpeta;
    @Size(max = 20)
    @Column(name = "numero")
    private String numero;
    @Size(max = 20)
    @Column(name = "repertorio")
    private String repertorio;
    @Size(max = 10)
    @Column(name = "letra")
    private String letra;
    @Size(min = 1, max = 500)
    @Column(name = "ruta_anterior")
    private String rutaAnterior;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne
    private RegMovimiento movimiento;
    @Column(name = "id_transaccion")
    private Long idTransaccion;
    @Column(name = "pagina")
    private Integer pagina;

    public RegMovimientoFile() {
    }

    public RegMovimientoFile(Long id) {
        this.id = id;
    }

    public RegMovimientoFile(Long id, long oidFile, String nombreArchivo, Date fechaCreacion, String rutaAnterior) {
        this.id = id;
        this.oidFile = oidFile;
        this.nombreArchivo = nombreArchivo;
        this.fechaCreacion = fechaCreacion;
        this.rutaAnterior = rutaAnterior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOidFile() {
        return oidFile;
    }

    public void setOidFile(long oidFile) {
        this.oidFile = oidFile;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIdtras() {
        return idtras;
    }

    public void setIdtras(String idtras) {
        this.idtras = idtras;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(String repertorio) {
        this.repertorio = repertorio;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getRutaAnterior() {
        return rutaAnterior;
    }

    public void setRutaAnterior(String rutaAnterior) {
        this.rutaAnterior = rutaAnterior;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
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
        if (!(object instanceof RegMovimientoFile)) {
            return false;
        }
        RegMovimientoFile other = (RegMovimientoFile) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoFile[ id=" + id + " ]";
    }

}
