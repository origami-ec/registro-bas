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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "orden_fichas", schema = "bitacora")
public class OrdenFichas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_orden")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOrden;
    @Column(name = "tipo_orden")
    private Integer tipoOrden;
    @Column(name = "estado")
    private Long estado;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "tramite")
    private Long tramite;
    @Column(name = "movimiento")
    private Long movimiento;
    @Column(name = "ficha")
    private Long ficha;
    @Column(name = "certificado")
    private Long certificado;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AclUser usuario;
    @Column(name = "num_orden")
    private Integer numOrden;
    @Column(name = "mes_orden")
    private Integer mesOrden;
    @Column(name = "anio_orden")
    private Integer anioOrden;
    @OneToMany(mappedBy = "ordenFicha", fetch = FetchType.LAZY)
    @OrderBy("fecha_inicio ASC")
    private Collection<OrdenFichasDetalle> ordenFichasDetallesCollection;

    public OrdenFichas() {
    }

    public String getPeriodo() {
        return anioOrden + "-" + mesOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public Integer getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(Integer tipoOrden) {
        this.tipoOrden = tipoOrden;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Long movimiento) {
        this.movimiento = movimiento;
    }

    public Long getFicha() {
        return ficha;
    }

    public void setFicha(Long ficha) {
        this.ficha = ficha;
    }

    public Long getCertificado() {
        return certificado;
    }

    public void setCertificado(Long certificado) {
        this.certificado = certificado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public AclUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AclUser usuario) {
        this.usuario = usuario;
    }

    public Integer getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(Integer numOrden) {
        this.numOrden = numOrden;
    }

    public Integer getMesOrden() {
        return mesOrden;
    }

    public void setMesOrden(Integer mesOrden) {
        this.mesOrden = mesOrden;
    }

    public Integer getAnioOrden() {
        return anioOrden;
    }

    public void setAnioOrden(Integer anioOrden) {
        this.anioOrden = anioOrden;
    }

    public Collection<OrdenFichasDetalle> getOrdenFichasDetallesCollection() {
        return ordenFichasDetallesCollection;
    }

    public void setOrdenFichasDetallesCollection(Collection<OrdenFichasDetalle> ordenFichasDetallesCollection) {
        this.ordenFichasDetallesCollection = ordenFichasDetallesCollection;
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
        if (!(object instanceof OrdenFichas)) {
            return false;
        }
        OrdenFichas other = (OrdenFichas) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.OrdenFichas[ id=" + id + " ]";
    }
}
