/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_tareas_dinardap", schema = "flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegpTareasDinardap.findAll", query = "SELECT r FROM RegpTareasDinardap r"),
    @NamedQuery(name = "RegpTareasDinardap.findById", query = "SELECT r FROM RegpTareasDinardap r WHERE r.id = :id"),
    @NamedQuery(name = "RegpTareasDinardap.findByInstitucion", query = "SELECT r FROM RegpTareasDinardap r WHERE r.institucion = :institucion"),
    @NamedQuery(name = "RegpTareasDinardap.findBySolicitante", query = "SELECT r FROM RegpTareasDinardap r WHERE r.solicitante = :solicitante"),
    @NamedQuery(name = "RegpTareasDinardap.findByNumeroSolicitud", query = "SELECT r FROM RegpTareasDinardap r WHERE r.numeroSolicitud = :numeroSolicitud"),
    @NamedQuery(name = "RegpTareasDinardap.findByDocumento", query = "SELECT r FROM RegpTareasDinardap r WHERE r.documento = :documento"),
    @NamedQuery(name = "RegpTareasDinardap.findByObservacion", query = "SELECT r FROM RegpTareasDinardap r WHERE r.observacion = :observacion"),
    @NamedQuery(name = "RegpTareasDinardap.findByDescripcion", query = "SELECT r FROM RegpTareasDinardap r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RegpTareasDinardap.findByFecha", query = "SELECT r FROM RegpTareasDinardap r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "RegpTareasDinardap.findByUsuario", query = "SELECT r FROM RegpTareasDinardap r WHERE r.usuario = :usuario"),
    @NamedQuery(name = "RegpTareasDinardap.findByRealizado", query = "SELECT r FROM RegpTareasDinardap r WHERE r.realizado = :realizado"),
    @NamedQuery(name = "RegpTareasDinardap.findByFechaFin", query = "SELECT r FROM RegpTareasDinardap r WHERE r.fechaFin = :fechaFin"),
    @NamedQuery(name = "RegpTareasDinardap.findByEstado", query = "SELECT r FROM RegpTareasDinardap r WHERE r.estado = :estado")})
public class RegpTareasDinardap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 500)
    @Column(name = "institucion")
    private String institucion;
    @Size(max = 500)
    @Column(name = "solicitante")
    private String solicitante;
    @Size(max = 500)
    @Column(name = "numero_solicitud")
    private String numeroSolicitud;
    @Size(max = 500)
    @Column(name = "documento")
    private String documento;
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 100)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "realizado")
    private boolean realizado;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tareaDinardap", fetch = FetchType.LAZY)
    private Collection<RegpTareasDinardapDocs> regpTareasDinardapDocsCollection;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacion liquidacion;
    @JoinColumn(name = "acto", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegActo acto;
    @Column(name = "es_juridico")
    private Boolean esJuridico;
    @Column(name = "estado_tramite")
    private String estadoTramite;
    @Column(name = "ticket")
    private String ticket;
    @Column(name = "fecha_despacho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDespacho;

    public RegpTareasDinardap() {
    }

    public RegpTareasDinardap(Long id) {
        this.id = id;
    }

    public RegpTareasDinardap(Long id, boolean realizado) {
        this.id = id;
        this.realizado = realizado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<RegpTareasDinardapDocs> getRegpTareasDinardapDocsCollection() {
        return regpTareasDinardapDocsCollection;
    }

    public void setRegpTareasDinardapDocsCollection(Collection<RegpTareasDinardapDocs> regpTareasDinardapDocsCollection) {
        this.regpTareasDinardapDocsCollection = regpTareasDinardapDocsCollection;
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
        if (!(object instanceof RegpTareasDinardap)) {
            return false;
        }
        RegpTareasDinardap other = (RegpTareasDinardap) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegpTareasDinardap[ id=" + id + " ]";
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public Boolean getEsJuridico() {
        return esJuridico;
    }

    public void setEsJuridico(Boolean esJuridico) {
        this.esJuridico = esJuridico;
    }

    public String getEstadoTramite() {
        return estadoTramite;
    }

    public void setEstadoTramite(String estadoTramite) {
        this.estadoTramite = estadoTramite;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }
    
}
