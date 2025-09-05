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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "regp_tareas_tramite", schema = "flow")
public class RegpTareasTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites tramite;
    @JoinColumn(name = "detalle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegpLiquidacionDetalles detalle;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "realizado")
    private Boolean realizado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "documento")
    private Boolean documento = false;
    @Column(name = "corregido")
    private Boolean corregido = false;
    @Column(name = "ficha")
    private Boolean ficha = false;
    @Column(name = "ruta_documento")
    private String rutaDocumento;

    @Transient
    private Boolean reemplazo = false;

    @Column(name = "fecha_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRevision;
    @Column(name = "revisado")
    private Boolean revisado = false;

    @OneToOne(mappedBy = "tareaTramite", fetch = FetchType.LAZY)
    private RegCertificado certificado;
    @OneToOne(mappedBy = "tramite", fetch = FetchType.LAZY)
    private RegMovimiento movimiento;

    public RegpTareasTramite() {
    }

    public RegpTareasTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public RegpLiquidacionDetalles getDetalle() {
        return detalle;
    }

    public void setDetalle(RegpLiquidacionDetalles detalle) {
        this.detalle = detalle;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public Boolean getReemplazo() {
        return reemplazo;
    }

    public void setReemplazo(Boolean reemplazo) {
        this.reemplazo = reemplazo;
    }

    public Boolean getCorregido() {
        return corregido;
    }

    public void setCorregido(Boolean corregido) {
        this.corregido = corregido;
    }

    public Boolean getFicha() {
        return ficha;
    }

    public void setFicha(Boolean ficha) {
        this.ficha = ficha;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public RegCertificado getCertificado() {
        return certificado;
    }

    public void setCertificado(RegCertificado certificado) {
        this.certificado = certificado;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
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
        if (!(object instanceof RegpTareasTramite)) {
            return false;
        }
        RegpTareasTramite other = (RegpTareasTramite) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "RegpTareasTramite{" + "id=" + id + ", tramite=" + tramite + ", detalle=" + detalle + ", fecha=" + fecha + ", fechaFin=" + fechaFin + ", realizado=" + realizado + ", estado=" + estado + ", documento=" + documento + ", corregido=" + corregido + ", ficha=" + ficha + ", reemplazo=" + reemplazo + '}';
    }

}
