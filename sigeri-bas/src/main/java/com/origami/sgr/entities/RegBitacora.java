/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_bitacora", schema = "bitacora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegBitacora.findAll", query = "SELECT r FROM RegBitacora r"),
    @NamedQuery(name = "RegBitacora.findById", query = "SELECT r FROM RegBitacora r WHERE r.id = :id"),
    @NamedQuery(name = "RegBitacora.findByFecha", query = "SELECT r FROM RegBitacora r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "RegBitacora.findByFechaHora", query = "SELECT r FROM RegBitacora r WHERE r.fechaHora = :fechaHora"),
    @NamedQuery(name = "RegBitacora.findByIdUsuario", query = "SELECT r FROM RegBitacora r WHERE r.idUsuario = :idUsuario"),
    @NamedQuery(name = "RegBitacora.findByCodUsuario", query = "SELECT r FROM RegBitacora r WHERE r.codUsuario = :codUsuario"),
    @NamedQuery(name = "RegBitacora.findByCodPrograma", query = "SELECT r FROM RegBitacora r WHERE r.codPrograma = :codPrograma"),
    @NamedQuery(name = "RegBitacora.findByActividad", query = "SELECT r FROM RegBitacora r WHERE r.actividad = :actividad"),
    @NamedQuery(name = "RegBitacora.findByIdMovimiento", query = "SELECT r FROM RegBitacora r WHERE r.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "RegBitacora.findByCodLibro", query = "SELECT r FROM RegBitacora r WHERE r.codLibro = :codLibro"),
    @NamedQuery(name = "RegBitacora.findByNumInscripcion", query = "SELECT r FROM RegBitacora r WHERE r.numInscripcion = :numInscripcion"),
    @NamedQuery(name = "RegBitacora.findByFechaInscripcion", query = "SELECT r FROM RegBitacora r WHERE r.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "RegBitacora.findByIndice", query = "SELECT r FROM RegBitacora r WHERE r.indice = :indice"),
    @NamedQuery(name = "RegBitacora.findByRepertorioOrdenTramite", query = "SELECT r FROM RegBitacora r WHERE r.repertorioOrdenTramite = :repertorioOrdenTramite"),
    @NamedQuery(name = "RegBitacora.findByIdFicha", query = "SELECT r FROM RegBitacora r WHERE r.idFicha = :idFicha"),
    @NamedQuery(name = "RegBitacora.findByTipFicha", query = "SELECT r FROM RegBitacora r WHERE r.tipFicha = :tipFicha"),
    @NamedQuery(name = "RegBitacora.findByNumFicha", query = "SELECT r FROM RegBitacora r WHERE r.numFicha = :numFicha"),
    @NamedQuery(name = "RegBitacora.findByTipCliente", query = "SELECT r FROM RegBitacora r WHERE r.tipCliente = :tipCliente"),
    @NamedQuery(name = "RegBitacora.findByCedRuc", query = "SELECT r FROM RegBitacora r WHERE r.cedRuc = :cedRuc"),
    @NamedQuery(name = "RegBitacora.findByTipServicio", query = "SELECT r FROM RegBitacora r WHERE r.tipServicio = :tipServicio"),
    @NamedQuery(name = "RegBitacora.findByAnio", query = "SELECT r FROM RegBitacora r WHERE r.anio = :anio")})
public class RegBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Column(name = "id_usuario")
    private BigInteger idUsuario;
    @Column(name = "cod_usuario")
    private BigInteger codUsuario;
    @Size(max = 10)
    @Column(name = "cod_programa")
    private String codPrograma;
    @Column(name = "actividad")
    private String actividad;
    @Column(name = "id_movimiento")
    private BigInteger idMovimiento;
    @Column(name = "cod_libro")
    private BigInteger codLibro;
    @Column(name = "num_inscripcion")
    private BigInteger numInscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "indice")
    private BigInteger indice;
    @Column(name = "repertorio_orden_tramite")
    private BigInteger repertorioOrdenTramite;
    @Column(name = "id_ficha")
    private BigInteger idFicha;
    @Column(name = "tip_ficha")
    private BigInteger tipFicha;
    @Column(name = "num_ficha")
    private BigInteger numFicha;
    @Size(max = 1)
    @Column(name = "tip_cliente")
    private String tipCliente;
    @Size(max = 30)
    @Column(name = "ced_ruc")
    private String cedRuc;
    @Column(name = "tip_servicio")
    private BigInteger tipServicio;
    @Column(name = "anio")
    private BigInteger anio;
    @Column(name = "id_certificado")
    private BigInteger idCertificado;
    @Size(max = 100)
    @Column(name = "ip_cliente")
    private String ipCliente;
    @Size(max = 100)
    @Column(name = "mac_cliente")
    private String macCliente;

    public RegBitacora() {
    }

    public RegBitacora(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(BigInteger codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getCodPrograma() {
        return codPrograma;
    }

    public void setCodPrograma(String codPrograma) {
        this.codPrograma = codPrograma;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public BigInteger getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(BigInteger idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public BigInteger getCodLibro() {
        return codLibro;
    }

    public void setCodLibro(BigInteger codLibro) {
        this.codLibro = codLibro;
    }

    public BigInteger getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(BigInteger numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public BigInteger getIndice() {
        return indice;
    }

    public void setIndice(BigInteger indice) {
        this.indice = indice;
    }

    public BigInteger getRepertorioOrdenTramite() {
        return repertorioOrdenTramite;
    }

    public void setRepertorioOrdenTramite(BigInteger repertorioOrdenTramite) {
        this.repertorioOrdenTramite = repertorioOrdenTramite;
    }

    public BigInteger getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(BigInteger idFicha) {
        this.idFicha = idFicha;
    }

    public BigInteger getTipFicha() {
        return tipFicha;
    }

    public void setTipFicha(BigInteger tipFicha) {
        this.tipFicha = tipFicha;
    }

    public BigInteger getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(BigInteger numFicha) {
        this.numFicha = numFicha;
    }

    public String getTipCliente() {
        return tipCliente;
    }

    public void setTipCliente(String tipCliente) {
        this.tipCliente = tipCliente;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public BigInteger getTipServicio() {
        return tipServicio;
    }

    public void setTipServicio(BigInteger tipServicio) {
        this.tipServicio = tipServicio;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(BigInteger idCertificado) {
        this.idCertificado = idCertificado;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public String getMacCliente() {
        return macCliente;
    }

    public void setMacCliente(String macCliente) {
        this.macCliente = macCliente;
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
        if (!(object instanceof RegBitacora)) {
            return false;
        }
        RegBitacora other = (RegBitacora) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegBitacora[ id=" + id + " ]";
    }

}
