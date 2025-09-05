/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Arturo
 */
@Entity
@Table(name = "solicitud_servicios", schema = "ventanilla")
public class SolicitudServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "solicitud_interna")
    private Boolean solicitudInterna;
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private CatEnte solicitante;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private HistoricoTramites tramite;
    @JoinColumn(name = "usuario_ingreso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private AclUser usuarioIngreso;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Servicio servicio;
    @Column(name = "finalizado")
    private Boolean finalizado;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RegpLiquidacion liquidacion;
    @Column(name = "tipo_contribuyente")
    private String tipoContribuyente;
    @Column(name = "en_observacion")
    private Boolean enObservacion;
    @Column(name = "pendiente_pago")
    private Boolean pendientePago;
    @Column(name = "documento")
    private Long documento;

    public SolicitudServicios() {
    }

    public SolicitudServicios(Long id) {
        this.id = id;
    }

    public Boolean getPendientePago() {
        return pendientePago;
    }

    public void setPendientePago(Boolean pendientePago) {
        this.pendientePago = pendientePago;
    }

    public Boolean getEnObservacion() {
        return enObservacion;
    }

    public void setEnObservacion(Boolean enObservacion) {
        this.enObservacion = enObservacion;
    }

    public String getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(String tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public AclUser getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(AclUser usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
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
        if (!(object instanceof SolicitudServicios)) {
            return false;
        }
        SolicitudServicios other = (SolicitudServicios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ventanilla.Entity.SolicitudServicios[ id=" + id + " ]";
    }

}
